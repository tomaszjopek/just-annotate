package pl.itj.dev.justannotatebackend.adapter.mongo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import pl.itj.dev.justannotatebackend.domain.Label
import pl.itj.dev.justannotatebackend.domain.Project
import pl.itj.dev.justannotatebackend.domain.ProjectType
import pl.itj.dev.justannotatebackend.domain.ports.ProjectRepository

@Repository
@ConditionalOnProperty(prefix = "spring.data.mongodb", name = ["uri"])
interface InternalProjectRepository : CoroutineCrudRepository<ProjectDocument, String> {

    fun findAllByOwner(owner: String): Flow<ProjectDocument>

}

@Repository
@ConditionalOnProperty(prefix = "spring.data.mongodb", name = ["uri"])
class MongoProjectRepository(private val internalProjectRepository: InternalProjectRepository) : ProjectRepository {

    override fun findAll(): Flow<Project> {
        return internalProjectRepository.findAll()
                .map { it.toDomain() }
    }

    override fun findAllByOwner(owner: String): Flow<Project> {
        return internalProjectRepository.findAllByOwner(owner)
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
                lastModifiedDate = lastModifiedDate,
                labels = labels.map { it.toDomain() }.toSet()
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
                lastModifiedDate = lastModifiedDate,
                labels = labels.map { it.toDocument() }.toSet()
        )
    }

    private fun LabelDocument.toDomain(): Label {
        return Label(
                name = name,
                color = color
        )
    }

    private fun Label.toDocument(): LabelDocument {
        return LabelDocument(
                name = name,
                color = color
        )
    }
}
