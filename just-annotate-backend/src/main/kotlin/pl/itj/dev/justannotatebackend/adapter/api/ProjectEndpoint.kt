package pl.itj.dev.justannotatebackend.adapter.api

import jakarta.validation.Valid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.http.codec.multipart.FilePart
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*
import pl.itj.dev.justannotatebackend.adapter.api.exceptions.ObjectNotFound
import pl.itj.dev.justannotatebackend.domain.Project
import pl.itj.dev.justannotatebackend.domain.ProjectType
import pl.itj.dev.justannotatebackend.domain.ports.DatasetItemRepository
import pl.itj.dev.justannotatebackend.domain.ports.ProjectRepository
import pl.itj.dev.justannotatebackend.domain.services.CsvFileImporter
import pl.itj.dev.justannotatebackend.infrastructure.security.username
import java.time.Clock
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("/projects")
class ProjectEndpoint(
        private val projectRepository: ProjectRepository,
        private val datasetItemRepository: DatasetItemRepository,
        private val csvFileImporter: CsvFileImporter,
        private val clock: Clock
) {

    companion object : KLogging()

    @GetMapping
    @ResponseBody
    suspend fun fetchProjects(@AuthenticationPrincipal jwtAuthenticationToken: JwtAuthenticationToken): Flow<ProjectResponse> {
        return projectRepository.findAllByOwner(jwtAuthenticationToken.username())
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
    suspend fun createProject(
            @Valid @RequestBody body: ProjectCreateRequest,
            @AuthenticationPrincipal jwtAuthenticationToken: JwtAuthenticationToken
    ): ProjectResponse {
        val project = body.toDomain(jwtAuthenticationToken.username())
        return projectRepository.save(project)
                .toResponse()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun deleteProject(@PathVariable id: String) {
        projectRepository.deleteById(id)
    }

    @PostMapping("/{id}/dataset/import")
    @ResponseStatus(HttpStatus.ACCEPTED)
    suspend fun importDataset(
            @PathVariable id: String,
            @RequestPart("file") file: FilePart,
            @AuthenticationPrincipal jwtAuthenticationToken: JwtAuthenticationToken) {
        logger.info { "Importing file: ${file.filename()} for user: ${jwtAuthenticationToken.username()}" }

        val texts = csvFileImporter.import(file.content())
        logger.info { "Collected ${texts.count()} lines" }

        datasetItemRepository.save(
                items = texts,
                projectId = id,
                username = jwtAuthenticationToken.username(),
                createdAt = LocalDateTime.ofInstant(clock.instant(), clock.zone),
                filename = file.filename()
        ).launchIn(CoroutineScope(Dispatchers.Default))
    }

    private fun Project.toResponse(): ProjectResponse {
        return ProjectResponse(
                id = id,
                name = name,
                description = description,
                type = type.name,
                owner = owner,
                createdAt = createdAt
        )
    }

    private fun ProjectCreateRequest.toDomain(username: String): Project {
        return Project(
                id = UUID.randomUUID().toString(),
                name = name,
                description = description,
                type = ProjectType.valueOf(type),
                owner = username,
                createdAt = LocalDateTime.ofInstant(clock.instant(), clock.zone),
                lastModifiedDate = LocalDateTime.ofInstant(clock.instant(), clock.zone)
        )
    }
}
