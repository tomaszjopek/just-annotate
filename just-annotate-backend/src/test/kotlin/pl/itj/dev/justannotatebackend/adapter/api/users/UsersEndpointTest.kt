package pl.itj.dev.justannotatebackend.adapter.api.users

import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration
import org.springframework.boot.context.properties.bind.Bindable
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.*
import org.springframework.test.web.reactive.server.WebTestClient
import pl.itj.dev.justannotatebackend.domain.User
import pl.itj.dev.justannotatebackend.infrastructure.clients.KeycloakClient


@WebFluxTest(UsersEndpoint::class)
@AutoConfigureWebTestClient
@EnableAutoConfiguration(exclude = [MongoReactiveAutoConfiguration::class, MongoReactiveDataAutoConfiguration::class])
class UsersEndpointTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockBean
    private lateinit var keycloakClient: KeycloakClient

//    @MockBean
//    private lateinit var reactiveOAuth2AuthorizedClientService: ReactiveOAuth2AuthorizedClientService
//
//    @MockBean
//    private lateinit var authorizedClient: OAuth2AuthorizedClient

//    @BeforeEach
//    fun setUp() {
//        whenever(oAuth2AuthorizedClient.clientRegistration)
//            .thenReturn(ClientRegistration.withRegistrationId("keycloak")
//                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                .clientId("just-annotate-backend")
//                .tokenUri("tetete")
//                .issuerUri("dasdasdsadas")
//                .jwkSetUri("dasdasdasdasd")
//                .clientSecret("dasdasd-asdasdasd-sadasd-asdas-das-dsadasd")
//                .build())
//
//        val oauthTokenMock = mock(OAuth2AccessToken::class.java)
//        whenever(oauthTokenMock.tokenType).thenReturn(TokenType.BEARER)
//        whenever(oAuth2AuthorizedClient.accessToken).thenReturn(oauthTokenMock)
//    }

    @Test
    fun `fetches users with optional username filter`() {
        val alice = User(id = "1", username = "alice")
        val bob = User(id = "2", username = "bob")
        val expectedUsers = flowOf(alice, bob)

        whenever(keycloakClient.searchUsers(any(), any())).thenReturn(expectedUsers)

        webTestClient
            .mutateWith(mockJwt().authorities(SimpleGrantedAuthority("SCOPE_admin")))
//            .mutateWith(mockOAuth2Client("keycloak")
//                .accessToken(OAuth2AccessToken(BEARER, "token", null, null, setOf("admin")))
//            )
            .get()
            .uri("/users")
            .exchange()
            .expectStatus().isOk()

    }

//    @Test
//    @Ignore
//    fun `filters users by username`() {
//        val bob = User(id = "3", username = "bob")
//        val expectedUsers = flowOf(bob)
//
//        whenever(keycloakClient.searchUsers("bob", any())).thenReturn(expectedUsers)
//
//        webTestClient.get().uri("/users?username=bob")
//                .exchange()
//                .expectStatus().isOk()
//                .expectBodyList(UserResponse::class.java)
//                .contains(UserResponse(username = "bob"))
//    }
//
//    @Test
//    @Ignore
//    fun `handles empty results gracefully`() = runTest {
//        val emptyResponse = emptyFlow<User>()
//
//        whenever(keycloakClient.searchUsers(any(), any())).thenReturn(emptyResponse)
//
//        val result = mutableListOf<UserResponse>()
//
//        webTestClient.get().uri("/users")
//                .exchange()
//                .expectStatus().isOk()
//                .returnResult(UserResponse::class.java)
//                .responseBody
//                .asFlow()
//                .toList(result)
//
//        assertEquals(0, result.size)
//    }
}
