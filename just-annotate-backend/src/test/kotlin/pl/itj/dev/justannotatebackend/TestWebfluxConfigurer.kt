package pl.itj.dev.justannotatebackend

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.core.MethodParameter
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.web.reactive.BindingContext
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class MockOAuthAuthorizedClientMethodHandler : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.getParameterAnnotation(RegisteredOAuth2AuthorizedClient::class.java) != null

    override fun resolveArgument(
        parameter: MethodParameter,
        bindingContext: BindingContext,
        exchange: ServerWebExchange
    ): Mono<Any> = Mono.just(
        OAuth2AuthorizedClient(
            ClientRegistration.withRegistrationId("keycloak")
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .clientId("just-annotate-backend")
                .tokenUri("http://localhost:9000/tokenUri")
                .build(),
            "test-app",
            OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "token", null, null)
        )
    )
}

@TestConfiguration
class TestWebfluxConfigurer : WebFluxConfigurer {

    override fun configureArgumentResolvers(configurer: ArgumentResolverConfigurer) {
        configurer.addCustomResolver(MockOAuthAuthorizedClientMethodHandler())
        super.configureArgumentResolvers(configurer)
    }
}
