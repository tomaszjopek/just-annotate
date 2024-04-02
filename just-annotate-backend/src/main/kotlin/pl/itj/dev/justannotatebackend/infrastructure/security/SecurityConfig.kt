package pl.itj.dev.justannotatebackend.infrastructure.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfig {

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:4200")
        configuration.allowCredentials = true
        configuration.allowedMethods = listOf("*")
        configuration.allowedHeaders = listOf("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())

        http.cors { it.configurationSource(corsConfigurationSource()) }

        http.csrf { it.disable() }

        http.logout { it.disable() }

        http.authorizeExchange {
            it.pathMatchers("/projects")
                    .hasAuthority("SCOPE_admin")
                    .pathMatchers("/users")
                    .hasAuthority("SCOPE_admin")
                    .anyExchange().authenticated()
        }

        http.oauth2ResourceServer { it.jwt { Customizer.withDefaults<ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec>() } }

        http.oauth2Client { Customizer.withDefaults<ServerHttpSecurity.OAuth2ClientSpec>() }

        return http.build()
    }

}