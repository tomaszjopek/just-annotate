package pl.itj.dev.justannotatebackend.adapter.mongo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import mu.KLogging
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import pl.itj.dev.justannotatebackend.domain.Annotation
import pl.itj.dev.justannotatebackend.domain.DatasetItem
import pl.itj.dev.justannotatebackend.domain.ports.DatasetItemRepository
import java.time.LocalDateTime
import java.util.UUID

@Repository
interface InternalDatasetItemRepository : CoroutineCrudRepository<DatasetItemDocument, String> {

    fun findAllByProjectId(projectId: String, pageable: Pageable): Flow<DatasetItemDocument>

    suspend fun countAllByProjectId(projectId: String): Long

    suspend fun findByIdAndProjectId(id: String, projectId: String): DatasetItemDocument

}

@Repository
class MongoDataSetItemRepository(private val internalDatasetItemRepository: InternalDatasetItemRepository) : DatasetItemRepository {

    companion object : KLogging()

    override fun findAll(): Flow<DatasetItem> {
        return internalDatasetItemRepository.findAll()
                .map { it.toDomain() }
    }

    override suspend fun save(datasetItem: DatasetItem): DatasetItem {
        return internalDatasetItemRepository.save(datasetItem.toDocument())
                .toDomain()
    }

    override fun save(items: Sequence<String>,
                      projectId: String,
                      username: String,
                      createdAt: LocalDateTime,
                      filename: String): Flow<DatasetItem> {
        val documents = items.map {
            DatasetItemDocument(
                    id = UUID.randomUUID().toString(),
                    projectId = projectId,
                    text = it,
                    annotations = emptyList(),
                    createdBy = username,
                    createdAt = createdAt,
                    originalFilename = filename
            )
        }.asFlow()

        logger.info { "Saving ${items.count()} dataset items" }
        return internalDatasetItemRepository.saveAll(documents)
                .map { it.toDomain() }
    }

    override fun findAllByProjectId(projectId: String, pageable: Pageable): Flow<DatasetItem> {
        return internalDatasetItemRepository.findAllByProjectId(projectId, pageable)
                .map { it.toDomain() }
    }

    override suspend fun countAllByProjectId(projectId: String): Long {
        return internalDatasetItemRepository.countAllByProjectId(projectId)
    }

    override suspend fun findByIdAndProjectId(id: String, projectId: String): DatasetItem {
        return internalDatasetItemRepository.findByIdAndProjectId(id, projectId)
                .toDomain()
    }

    private fun DatasetItemDocument.toDomain(): DatasetItem {
        return DatasetItem(
                id = id,
                projectId = projectId,
                text = text,
                annotations = annotations.map { it.toDomain() },
                createdBy = createdBy,
                createdAt = createdAt,
                originalFilename = originalFilename
        )
    }

    private fun AnnotationDocument.toDomain(): Annotation {
        return Annotation(
                label = label,
                annotator = annotator,
                createdAt = createdAt
        )
    }

    private fun DatasetItem.toDocument(): DatasetItemDocument {
        return DatasetItemDocument(
                id = id,
                projectId = projectId,
                text = text,
                annotations = annotations.map { it.toDocument() },
                createdBy = createdBy,
                createdAt = createdAt,
                originalFilename = originalFilename
        )
    }

    private fun Annotation.toDocument(): AnnotationDocument {
        return AnnotationDocument(
                label = label,
                annotator = annotator,
                createdAt = createdAt
        )
    }
}
