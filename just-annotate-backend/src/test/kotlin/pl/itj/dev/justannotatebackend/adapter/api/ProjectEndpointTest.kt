package pl.itj.dev.justannotatebackend.adapter.api

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import pl.itj.dev.justannotatebackend.adapter.api.exceptions.ObjectNotFound
import pl.itj.dev.justannotatebackend.domain.Project
import pl.itj.dev.justannotatebackend.domain.ports.ProjectRepository

@ExtendWith(MockitoExtension::class)
class ProjectEndpointTest {

    private val projectRepository: ProjectRepository = mock()

    private val projectEndpoint = ProjectEndpoint(projectRepository)

    @Test
    fun `fetchProjects should return all projects`() = runTest {
        val projects = listOf(
                Project("1", "Project 1"),
                Project("2", "Project 2")
        )
        whenever(projectRepository.findAll()).thenReturn(projects.asFlow())

        val response = projectEndpoint.fetchProjects()

        val expectedResponse = listOf(
                ProjectResponse(id = "1", name = "Project 1"),
                ProjectResponse(id = "2", name = "Project 2")
        )

        assertEquals(expectedResponse, response.toList())
    }

    @Test
    fun `fetchProject with existing ID should return the project`() = runTest {
        val id = "1"
        val project = Project(id, "Project 1")
        whenever(projectRepository.findById(id)).thenReturn(project)

        val response = projectEndpoint.fetchProject(id)

        val expectedResponse = ProjectResponse(id = "1", name = "Project 1")
        assertThat(response).isEqualTo(expectedResponse)
    }

    @Test
    fun `fetchProject with non-existent ID should throw ObjectNotFound`() = runTest {
        assertThrows<ObjectNotFound> {
            val id = "non-existent"
            whenever(projectRepository.findById(id)).thenReturn(null)

            projectEndpoint.fetchProject(id)
        }
    }

    @Test
    fun `createProject with valid request should create and return the project`() = runTest {
        val createRequest = ProjectCreateRequest("New Project")
        val id = "12312dsadasd"
        whenever(projectRepository.save(any())).thenReturn(Project(id = id, name = "New Project"))

        val response = projectEndpoint.createProject(createRequest)

        val expectedResponse = ProjectResponse(id, "New Project")
        assertThat(response).isEqualTo(expectedResponse)
    }

    @Test
    fun `deleteProject should delete the project by ID`() = runTest {
        val id = "1"

        projectEndpoint.deleteProject(id)

        verify(projectRepository).deleteById(id)
    }
}
