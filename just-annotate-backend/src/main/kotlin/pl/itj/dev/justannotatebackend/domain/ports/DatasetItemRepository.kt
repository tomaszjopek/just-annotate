package pl.itj.dev.justannotatebackend.domain.ports

import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import pl.itj.dev.justannotatebackend.adapter.mongo.DatasetItemDocument
import pl.itj.dev.justannotatebackend.domain.DatasetItem
import java.time.LocalDateTime

interface DatasetItemRepository {

    fun findAll(): Flow<DatasetItem>

    fun findAllByProjectId(projectId: String, pageable: Pageable): Flow<DatasetItem>

    suspend fun countAllByProjectId(projectId: String): Long

    fun save(items: Sequence<String>,
             projectId: String,
             username: String,
             createdAt: LocalDateTime,
             filename: String): Flow<DatasetItemDocument>

}