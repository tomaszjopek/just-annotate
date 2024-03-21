package pl.itj.dev.justannotatebackend.domain.ports

import kotlinx.coroutines.flow.Flow
import pl.itj.dev.justannotatebackend.adapter.mongo.DatasetItemDocument
import pl.itj.dev.justannotatebackend.domain.DatasetItem
import java.time.LocalDateTime

interface DatasetItemRepository {

    suspend fun findAll(): Flow<DatasetItem>

    fun save(items: Sequence<String>,
             projectId: String,
             username: String,
             createdAt: LocalDateTime,
             filename: String): Flow<DatasetItemDocument>

}