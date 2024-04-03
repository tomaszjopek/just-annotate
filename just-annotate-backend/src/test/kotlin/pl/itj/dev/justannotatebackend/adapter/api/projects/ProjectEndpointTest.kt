package pl.itj.dev.justannotatebackend.adapter.api.projects

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt
import org.springframework.test.web.reactive.server.WebTestClient
import pl.itj.dev.justannotatebackend.TestCommonConfig
import pl.itj.dev.justannotatebackend.TestWebfluxConfigurer
import pl.itj.dev.justannotatebackend.adapter.api.exceptions.ObjectNotFound
import pl.itj.dev.justannotatebackend.adapter.api.users.UserResponse
import pl.itj.dev.justannotatebackend.adapter.api.users.UsersEndpoint
import pl.itj.dev.justannotatebackend.domain.Project
import pl.itj.dev.justannotatebackend.domain.ProjectType
import pl.itj.dev.justannotatebackend.domain.ports.DatasetItemRepository
import pl.itj.dev.justannotatebackend.domain.ports.ProjectRepository
import pl.itj.dev.justannotatebackend.domain.services.CsvFileImporter
import java.time.Clock
import java.time.LocalDateTime

@WebFluxTest(ProjectEndpoint::class)
@Import(TestCommonConfig::class)
@EnableAutoConfiguration(exclude = [MongoReactiveAutoConfiguration::class, MongoReactiveDataAutoConfiguration::class])
class ProjectEndpointTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var clock: Clock

    @MockBean
    private lateinit var projectRepository: ProjectRepository

    @MockBean
    private lateinit var datasetItemRepository: DatasetItemRepository

    @MockBean
    private lateinit var csvFileImporter: CsvFileImporter

    @Test
    fun `should return unauthorized`() {
        webTestClient.get()
            .uri("/projects")
            .exchange()
            .expectStatus().isUnauthorized()
    }

    @Test
    fun `should return forbidden for user with insufficient privileges`() {
        webTestClient.mutateWith(mockJwt())
            .get()
            .uri("/projects")
            .exchange()
            .expectStatus().isForbidden()
    }

    @Test
    fun `should return empty list for authenticated user and no projects`() {
        whenever(projectRepository.findAllByOwner(eq("testuser"))).thenReturn(emptyFlow())

        webTestClient.mutateWith(mockJwt()
            .jwt { it.claim("preferred_username", "testuser") }
            .authorities(SimpleGrantedAuthority("SCOPE_admin"))
        )
            .get()
            .uri("/projects")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(UserResponse::class.java)
            .hasSize(0)
    }

    @Test
    fun `should return projects for authenticated user`() {
        val givenProjects = flowOf(
            Project(
                id = "dasd1212-dsa-ddsad-dsadas",
                name = "project1",
                description = "description",
                type = ProjectType.TEXT,
                owner = "testuser",
                createdAt = LocalDateTime.ofInstant(clock.instant(), clock.zone),
                lastModifiedDate = LocalDateTime.ofInstant(clock.instant(), clock.zone),
                labels = emptySet()
            )
        )

        whenever(projectRepository.findAllByOwner(eq("testuser"))).thenReturn(givenProjects)

        webTestClient.mutateWith(mockJwt()
            .jwt { it.claim("preferred_username", "testuser") }
            .authorities(SimpleGrantedAuthority("SCOPE_admin"))
        )
            .get()
            .uri("/projects")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ProjectResponse::class.java)
            .hasSize(1)
            .contains(
                ProjectResponse(
                    id = "dasd1212-dsa-ddsad-dsadas",
                    name = "project1",
                    description = "description",
                    type = "TEXT",
                    owner = "testuser",
                    createdAt = LocalDateTime.ofInstant(clock.instant(), clock.zone),
                    labels = emptySet()
                )
            )
    }
}
