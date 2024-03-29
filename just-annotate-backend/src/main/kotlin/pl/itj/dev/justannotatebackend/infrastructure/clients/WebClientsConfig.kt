package pl.itj.dev.justannotatebackend.infrastructure.clients

import mu.KLogging
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository
import org.springframework.web.reactive.function.client.*
import pl.itj.dev.justannotatebackend.infrastructure.properties.KeycloakClientProperties
import reactor.core.publisher.Mono

@Configuration
@EnableConfigurationProperties(KeycloakClientProperties::class)
class WebClientsConfig {

    companion object : KLogging()
    @Bean
    fun keycloakWebClient(
            webclientBuilder: WebClient.Builder,
            keycloakClientProperties: KeycloakClientProperties,
            reactiveClientRegistrationRepository: ReactiveClientRegistrationRepository,
            authorizedClientRepository: ServerOAuth2AuthorizedClientRepository): WebClient {
        val oauthFilter = ServerOAuth2AuthorizedClientExchangeFilterFunction(reactiveClientRegistrationRepository, authorizedClientRepository)
        oauthFilter.setDefaultClientRegistrationId("keycloak")

        return webclientBuilder
                .baseUrl(keycloakClientProperties.baseUrl)
                .filter(logRequest())
                .filter(logResponse())
                .filter(oauthFilter)
                .build()
    }

    private fun logRequest(): ExchangeFilterFunction {
        return ExchangeFilterFunction { clientRequest: ClientRequest, next: ExchangeFunction ->
            logger.debug { "Request: ${clientRequest.method()} ${clientRequest.url()}" }
            logger.debug { "Request headers: ${clientRequest.headers()}" }
            next.exchange(clientRequest)
        }
    }

    private fun logResponse(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofResponseProcessor { clientResponse: ClientResponse ->
            logger.debug { "Response Status: ${clientResponse.statusCode()}" }
            Mono.just(clientResponse)
        }
    }
}
