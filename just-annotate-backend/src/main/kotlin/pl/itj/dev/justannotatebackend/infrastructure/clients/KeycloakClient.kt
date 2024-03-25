package pl.itj.dev.justannotatebackend.infrastructure.clients

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlow
import pl.itj.dev.justannotatebackend.domain.User

@Component
class KeycloakClient(private val keycloakWebClient: WebClient) {

    fun searchUsers(username: String?, authorizedClient: OAuth2AuthorizedClient): Flow<User> {
        return keycloakWebClient.get()
                .uri("/users?username={username}", username)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToFlow<KeycloakUser>()
                .map { User(id = it.id, username = it.username) }
    }

    private data class KeycloakUser(
            val id: String,
            val username: String
    )
}
