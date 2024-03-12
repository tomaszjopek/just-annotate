package pl.itj.dev.justannotatebackend.adapter.mongo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import pl.itj.dev.justannotatebackend.domain.Project
import pl.itj.dev.justannotatebackend.domain.ProjectType
import pl.itj.dev.justannotatebackend.domain.ports.ProjectRepository

@Repository
interface InternalProjectRepository : CoroutineCrudRepository<ProjectDocument, String>

@Repository
class MongoProjectRepository(private val internalProjectRepository: InternalProjectRepository) : ProjectRepository {

    override suspend fun findAll(): Flow<Project> {
        return internalProjectRepository.findAll()
                .map { it.toDomain() }
    }

    override suspend fun findById(id: String): Project? {
        return internalProjectRepository.findById(id)
                ?.toDomain()
    }

    override suspend fun save(project: Project): Project {
        return internalProjectRepository.save(project.toDocument())
                .toDomain()
    }

    override suspend fun deleteById(id: String) {
        internalProjectRepository.deleteById(id)
    }

    private fun ProjectDocument.toDomain(): Project {
        return Project(
                id = id,
                name = name,
                description = description,
                type = ProjectType.valueOf(type),
                owner = owner,
                createdAt = createdAt,
                lastModifiedDate = lastModifiedDate
        )
    }

    private fun Project.toDocument(): ProjectDocument {
        return ProjectDocument(
                id = id,
                name = name,
                description = description,
                type = type.name,
                owner = owner,
                createdAt = createdAt,
                lastModifiedDate = lastModifiedDate
        )
    }
}
