package pl.itj.dev.justannotatebackend.infrastructure

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.session.SessionRegistry
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.session.HttpSessionEventPublisher


@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfig {

    @Bean
    fun sessionRegistry(): SessionRegistry {
        return SessionRegistryImpl()
    }

    @Bean
    protected fun sessionAuthenticationStrategy(): SessionAuthenticationStrategy {
        return RegisterSessionAuthenticationStrategy(sessionRegistry())
    }

    @Bean
    fun httpSessionEventPublisher(): HttpSessionEventPublisher {
        return HttpSessionEventPublisher()
    }

    @Bean
    @Throws(Exception::class)
    fun configure(http: ServerHttpSecurity): SecurityWebFilterChain {
        http.csrf { it.disable() }

        http.cors { it.disable() }

        http.authorizeExchange {
            it
                    .pathMatchers("/projects")
                    .hasAuthority("SCOPE_admin")
                    .anyExchange().authenticated()

        }

        http.oauth2Client(Customizer.withDefaults())

        http.oauth2ResourceServer { it.jwt(Customizer.withDefaults()) }

        return http.build()
    }
}