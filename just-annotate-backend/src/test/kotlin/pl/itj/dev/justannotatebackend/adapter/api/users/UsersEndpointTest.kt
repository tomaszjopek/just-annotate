package pl.itj.dev.justannotatebackend.adapter.api.users

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.test.web.reactive.server.WebTestClient
import pl.itj.dev.justannotatebackend.domain.User
import pl.itj.dev.justannotatebackend.infrastructure.clients.KeycloakClient

@WebFluxTest(UsersEndpoint::class)
class UsersEndpointTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockBean
    private lateinit var keycloakClient: KeycloakClient

    @MockBean
    private lateinit var authorizedClient: OAuth2AuthorizedClient

    @Test
    fun `fetches users with optional username filter`() {
        val alice = User(id = "1", username = "alice")
        val bob = User(id = "2", username = "bob")
        val expectedUsers = flowOf(alice, bob)

        whenever(keycloakClient.searchUsers(any(), any())).thenReturn(expectedUsers)

        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponse::class.java)
                .contains(UserResponse(username = "alice"), UserResponse(username = "bob"))
    }

    @Test
    fun `filters users by username`() {
        val bob = User(id = "3", username = "bob")
        val expectedUsers = flowOf(bob)

        whenever(keycloakClient.searchUsers("bob", any())).thenReturn(expectedUsers)

        webTestClient.get().uri("/users?username=bob")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponse::class.java)
                .contains(UserResponse(username = "bob"))
    }

    @Test
    fun `handles empty results gracefully`() = runTest {
        val emptyResponse = emptyFlow<User>()

        whenever(keycloakClient.searchUsers(any(), any())).thenReturn(emptyResponse)

        val result = mutableListOf<UserResponse>()

        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk()
                .returnResult(UserResponse::class.java)
                .responseBody
                .asFlow()
                .toList(result)

        assertEquals(0, result.size)
    }
}
