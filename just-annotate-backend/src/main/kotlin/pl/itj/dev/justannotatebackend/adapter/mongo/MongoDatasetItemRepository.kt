package pl.itj.dev.justannotatebackend.adapter.mongo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import mu.KLogging
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import pl.itj.dev.justannotatebackend.domain.DatasetItem
import pl.itj.dev.justannotatebackend.domain.ports.DatasetItemRepository
import java.time.LocalDateTime
import java.util.*

@Repository
interface InternalDatasetItemRepository : CoroutineCrudRepository<DatasetItemDocument, String> {

    fun findAllByProjectId(projectId: String, pageable: Pageable): Flow<DatasetItem>

    suspend fun countAllByProjectId(projectId: String): Long

}

@Repository
class MongoDataSetItemRepository(private val internalDatasetItemRepository: InternalDatasetItemRepository) : DatasetItemRepository {

    companion object : KLogging()

    override fun findAll(): Flow<DatasetItem> {
        return internalDatasetItemRepository.findAll()
                .map { it.toDomain() }
    }

    override fun save(items: Sequence<String>,
                      projectId: String,
                      username: String,
                      createdAt: LocalDateTime,
                      filename: String): Flow<DatasetItemDocument> {
        val documents = items.map {
            DatasetItemDocument(
                    id = UUID.randomUUID().toString(),
                    projectId = projectId,
                    text = it,
                    annotations = emptyMap(),
                    createdBy = username,
                    createdAt = createdAt,
                    originalFilename = filename
            )
        }.asFlow()

        logger.info { "Saving ${items.count()} dataset items" }
        return internalDatasetItemRepository.saveAll(documents)
    }

    override fun findAllByProjectId(projectId: String, pageable: Pageable): Flow<DatasetItem> {
        return internalDatasetItemRepository.findAllByProjectId(projectId, pageable)
    }

    override suspend fun countAllByProjectId(projectId: String): Long {
        return internalDatasetItemRepository.countAllByProjectId(projectId)
    }

    private fun DatasetItemDocument.toDomain(): DatasetItem {
        return DatasetItem(
                id = id,
                projectId = projectId,
                text = text,
                annotations = annotations,
                createdBy = createdBy,
                createdAt = createdAt,
                originalFilename = originalFilename
        )
    }
}
