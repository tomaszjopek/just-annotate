package pl.itj.dev.justannotatebackend.adapter.api.projects

import jakarta.validation.Valid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import mu.KLogging
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.codec.multipart.FilePart
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*
import pl.itj.dev.justannotatebackend.adapter.api.exceptions.ObjectNotFound
import pl.itj.dev.justannotatebackend.domain.*
import pl.itj.dev.justannotatebackend.domain.Annotation
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
    fun fetchProjects(@AuthenticationPrincipal jwtAuthenticationToken: JwtAuthenticationToken): Flow<ProjectResponse> {
        logger.info { "Fetching all projects for user: ${jwtAuthenticationToken.username()}" }
        return projectRepository.findAllByOwner(jwtAuthenticationToken.username())
                .map { it.toResponse() }
    }

    @GetMapping("/{id}")
    @ResponseBody
    suspend fun fetchProject(@PathVariable id: String): ProjectResponse {
        logger.info { "Fetching project with id: $id" }
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
        logger.info { "Create project request: $body" }

        val project = body.toDomain(jwtAuthenticationToken.username())
        return projectRepository.save(project)
                .toResponse()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun deleteProject(@PathVariable id: String) {
        logger.info { "Deleting project with id: $id" }
        projectRepository.deleteById(id)
        logger.info { "Deleted project with id: $id" }
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

    @GetMapping("/{id}/dataset/items")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    suspend fun fetchAllDatasetItems(
            @PathVariable id: String,
            @AuthenticationPrincipal jwtAuthenticationToken: JwtAuthenticationToken,
            pageable: Pageable
    ): PageImpl<DatasetItemResponse> {
        logger.info { "Fetching dataset items for page: ${pageable.pageNumber} and size: ${pageable.pageSize}" }
        val datasetItems = datasetItemRepository.findAllByProjectId(id, pageable)
                .map { it.toResponse() }

        val allItems = datasetItemRepository.countAllByProjectId(id)

        logger.info { "Total items is: $allItems" }
        return PageImpl<DatasetItemResponse>(datasetItems.toList(), pageable, allItems)
    }

    @PostMapping("/{projectId}/dataset/items/{datasetItemId}/annotate")
    @ResponseStatus(HttpStatus.OK)
    suspend fun annotateItemWithLabel(
            @PathVariable projectId: String,
            @PathVariable datasetItemId: String,
            @Valid @RequestBody body: AnnotateRequest,
            @AuthenticationPrincipal jwtAuthenticationToken: JwtAuthenticationToken) {
        logger.info { "Adding label ${body.label} for dataset item id $datasetItemId within $projectId project" }

        val datasetItem = datasetItemRepository.findByIdAndProjectId(datasetItemId, projectId)
        val currentAnnotations = datasetItem.annotations

        currentAnnotations.addLast(
                Annotation(
                        label = body.label,
                        annotator = jwtAuthenticationToken.username(),
                        createdAt = LocalDateTime.ofInstant(clock.instant(), clock.zone)
                )
        )

        datasetItemRepository.save(datasetItem.copy(annotations = currentAnnotations))
    }

    private fun Project.toResponse(): ProjectResponse {
        return ProjectResponse(
                id = id,
                name = name,
                description = description,
                projectType = type.name,
                owner = owner,
                createdAt = createdAt,
                labels = labels.map { LabelJson(name = it.name, color = it.color) }.toSet()
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
                lastModifiedDate = LocalDateTime.ofInstant(clock.instant(), clock.zone),
                labels = labels.map { Label(name = it.name, color = it.color) }.toSet()
        )
    }

    private fun DatasetItem.toResponse(): DatasetItemResponse {
        return DatasetItemResponse(
                id = id,
                text = text,
                annotations = annotations.map { it.toResponse() }
        )
    }

    private fun Annotation.toResponse(): AnnotationResponse {
        return AnnotationResponse(
                label = label,
                annotator = annotator,
                createdAt = createdAt
        )
    }
}
