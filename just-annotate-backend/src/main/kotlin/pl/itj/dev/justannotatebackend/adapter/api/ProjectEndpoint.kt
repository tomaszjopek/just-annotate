package pl.itj.dev.justannotatebackend.adapter.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.itj.dev.justannotatebackend.domain.Project
import pl.itj.dev.justannotatebackend.domain.ports.ProjectRepository

@RestController
@RequestMapping("/projects")
class ProjectEndpoint(private val projectRepository: ProjectRepository) {

    @GetMapping
    suspend fun fetchProjects(): Flow<ProjectResponse> {
        return projectRepository.findAll()
                .map { it.toResponse() }
    }

    private fun Project.toResponse(): ProjectResponse {
        return ProjectResponse(
                id = id,
                name = name
        )
    }
}
