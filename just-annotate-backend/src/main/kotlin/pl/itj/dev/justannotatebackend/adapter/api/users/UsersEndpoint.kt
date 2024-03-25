package pl.itj.dev.justannotatebackend.adapter.api.users

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlow

@RestController
@RequestMapping("/users")
class UsersEndpoint(
        private val keycloakWebClient: WebClient
) {

    @GetMapping
    @ResponseBody
    fun fetchUsers(
            @RegisteredOAuth2AuthorizedClient("keycloak") authorizedClient: OAuth2AuthorizedClient,
            @RequestParam(name = "username", required = false) username: String?): Flow<UserResponse> {
       return keycloakWebClient.get()
               .uri("/users?username={username}", username)
               .attributes(oauth2AuthorizedClient(authorizedClient))
               .retrieve()
               .bodyToFlow<KeycloakUser>()
               .map { UserResponse(username = it.username) }
    }
}
