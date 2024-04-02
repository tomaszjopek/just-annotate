package pl.itj.dev.justannotatebackend.adapter.api.users

import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Client
import org.springframework.test.web.reactive.server.WebTestClient
import pl.itj.dev.justannotatebackend.TestWebfluxConfigurer
import pl.itj.dev.justannotatebackend.domain.User
import pl.itj.dev.justannotatebackend.infrastructure.clients.KeycloakClient


@WebFluxTest(UsersEndpoint::class)
@Import(TestWebfluxConfigurer::class)
@EnableAutoConfiguration(exclude = [MongoReactiveAutoConfiguration::class, MongoReactiveDataAutoConfiguration::class])
class UsersEndpointTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockBean
    private lateinit var keycloakClient: KeycloakClient

    @Test
    fun `fetches users with optional username filter`() {
        val alice = User(id = "1", username = "alice")
        val bob = User(id = "2", username = "bob")
        val expectedUsers = flowOf(alice, bob)

        whenever(keycloakClient.searchUsers(anyOrNull(), any())).thenReturn(expectedUsers)

        webTestClient
            .mutateWith(mockJwt().authorities(SimpleGrantedAuthority("SCOPE_admin")))
            .mutateWith(mockOAuth2Client("keycloak"))
            .get()
            .uri("/users")
            .exchange()
            .expectStatus().isOk()
    }

    @Test
    fun `filters users by username`() {
        val bob = User(id = "3", username = "bob")
        val expectedUsers = flowOf(bob)

        whenever(keycloakClient.searchUsers(eq("bob"), any())).thenReturn(expectedUsers)

        webTestClient.mutateWith(mockJwt().authorities(SimpleGrantedAuthority("SCOPE_admin")))
                .mutateWith(mockOAuth2Client("keycloak"))
                .get()
                .uri("/users?username=bob")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponse::class.java)
                .contains(UserResponse(username = "bob"))
    }

    @Test
    fun `handles empty results gracefully`() {
        val emptyResponse = emptyFlow<User>()

        whenever(keycloakClient.searchUsers(anyOrNull(), any())).thenReturn(emptyResponse)

        webTestClient.mutateWith(mockJwt().authorities(SimpleGrantedAuthority("SCOPE_admin")))
                .mutateWith(mockOAuth2Client("keycloak"))
                .get()
                .uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponse::class.java)
                .hasSize(0)
    }

    @Test
    fun `handles not authorized user`() {
        webTestClient.get()
                .uri("/users")
                .exchange()
                .expectStatus().isUnauthorized()
    }
}
