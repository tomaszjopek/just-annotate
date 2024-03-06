package pl.itj.dev.justannotatebackend.adapter.api

import jakarta.validation.Valid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import pl.itj.dev.justannotatebackend.adapter.api.exceptions.ObjectNotFound
import pl.itj.dev.justannotatebackend.domain.Project
import pl.itj.dev.justannotatebackend.domain.ports.ProjectRepository
import java.util.*

@RestController
@RequestMapping("/projects")
class ProjectEndpoint(private val projectRepository: ProjectRepository) {

    companion object : KLogging()

    @GetMapping
    @ResponseBody
    suspend fun fetchProjects(): Flow<ProjectResponse> {
        return projectRepository.findAll()
                .map { it.toResponse() }
    }

    @GetMapping("/{id}")
    @ResponseBody
    suspend fun fetchProject(@PathVariable id: String): ProjectResponse {
        return projectRepository.findById(id)
                ?.toResponse() ?: throw ObjectNotFound("Project with id: $id not found")
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    suspend fun createProject(@Valid @RequestBody body: ProjectCreateRequest): ProjectResponse {
        val project = body.toDomain()
        return projectRepository.save(project)
                .toResponse()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun deleteProject(@PathVariable id: String) {
        projectRepository.deleteById(id)
    }

    private fun Project.toResponse(): ProjectResponse {
        return ProjectResponse(
                id = id,
                name = name
        )
    }

    private fun ProjectCreateRequest.toDomain(): Project {
        return Project(
                id = UUID.randomUUID().toString(),
                name = name
        )
    }
}
