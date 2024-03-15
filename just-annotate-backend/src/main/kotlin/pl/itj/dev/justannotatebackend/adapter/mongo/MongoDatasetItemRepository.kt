package pl.itj.dev.justannotatebackend.adapter.mongo

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import pl.itj.dev.justannotatebackend.domain.DatasetItem
import pl.itj.dev.justannotatebackend.domain.ports.DatasetItemRepository
import java.time.LocalDateTime
import java.util.UUID

@Repository
interface InternalDatasetItemRepository : CoroutineCrudRepository<DatasetItemDocument, String>

@Repository
class MongoDataSetItemRepository(private val internalDatasetItemRepository: InternalDatasetItemRepository) : DatasetItemRepository {

    override suspend fun findAll(): Flow<DatasetItem> {
        return internalDatasetItemRepository.findAll()
                .map { it.toDomain() }
    }

    override suspend fun save(items: Sequence<String>,
                              projectId: String,
                              username: String,
                              createdAt: LocalDateTime) {
        val documents = items.map {
            DatasetItemDocument(
                    id = UUID.randomUUID().toString(),
                    projectId = projectId,
                    text = it,
                    annotations = emptyMap(),
                    createdBy = username,
                    createdAt = createdAt
            )
        }.asFlow()

        runBlocking { internalDatasetItemRepository.saveAll(documents) }
    }

    private fun DatasetItemDocument.toDomain(): DatasetItem {
        return DatasetItem(
                id = id,
                projectId = projectId,
                text = text,
                annotations = annotations,
                createdBy = createdBy,
                createdAt = createdAt
        )
    }
}
