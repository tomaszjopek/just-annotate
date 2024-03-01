package pl.itj.dev.justannotatebackend.adapter.mongo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import pl.itj.dev.justannotatebackend.domain.Project
import pl.itj.dev.justannotatebackend.domain.ports.ProjectRepository

@Repository
interface InternalProjectRepository : CoroutineCrudRepository<ProjectDocument, String>

@Repository
class MongoProjectRepository(private val internalProjectRepository: InternalProjectRepository) : ProjectRepository {

    override suspend fun findAll(): Flow<Project> {
        return internalProjectRepository.findAll()
                .map { it.toDomain() }
    }

    private fun ProjectDocument.toDomain(): Project {
        return Project(
                id = id ?: "",
                name = name
        )
    }
}
