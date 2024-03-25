package pl.itj.dev.justannotatebackend.adapter.api.users

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.web.bind.annotation.*
import pl.itj.dev.justannotatebackend.infrastructure.clients.KeycloakClient

@RestController
@RequestMapping("/users")
class UsersEndpoint(
        private val keycloakClient: KeycloakClient
) {

    @GetMapping
    @ResponseBody
    fun fetchUsers(
            @RegisteredOAuth2AuthorizedClient("keycloak") authorizedClient: OAuth2AuthorizedClient,
            @RequestParam(name = "username", required = false) username: String?): Flow<UserResponse> {
        return keycloakClient.searchUsers(username, authorizedClient)
                .map { UserResponse(username = it.username) }
    }
}
