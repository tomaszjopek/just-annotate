package pl.itj.dev.justannotatebackend

import org.mockito.kotlin.mock
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders

@TestConfiguration
class TestCommonConfig {

//    @Bean
//    fun inMemoryReactiveClientRegistrationRepository(): ReactiveClientRegistrationRepository {
//        return InMemoryReactiveClientRegistrationRepository(
//            ClientRegistration.withRegistrationId("keycloak")
//                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                .clientId("just-annotate-backend")
//                .tokenUri("http://localhost:9000/tokenUri")
//                .build()
//        )
//    }

}