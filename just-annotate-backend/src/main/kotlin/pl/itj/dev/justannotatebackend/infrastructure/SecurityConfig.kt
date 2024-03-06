package pl.itj.dev.justannotatebackend.infrastructure

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity.http
import org.springframework.security.oauth2.core.authorization.OAuth2ReactiveAuthorizationManagers.hasScope
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfig {

//    @Bean
//    fun corsWebFilter(): CorsWebFilter {
//        val corsConfig = CorsConfiguration()
//        corsConfig.allowedOrigins = mutableListOf("http://localhost:4200")
//        corsConfig.allowCredentials = true
//        corsConfig.maxAge = 8000L
//        corsConfig.addAllowedMethod("GET")
//        corsConfig.addAllowedMethod("POST")
//        corsConfig.addAllowedMethod("PUT")
//        corsConfig.addAllowedMethod("PATCH")
//        corsConfig.addAllowedMethod("DELETE")
//        corsConfig.addAllowedMethod("OPTIONS")
//        corsConfig.addAllowedHeader("Authorization")
//
//        val source = UrlBasedCorsConfigurationSource()
//        source.registerCorsConfiguration("/**", corsConfig)
//
//        return CorsWebFilter(source)
//    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:4200")
//        configuration.allowedMethods = listOf("GET", "POST")
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
                    .anyExchange().authenticated()
        }

        http.oauth2ResourceServer { it.jwt {  } }

        return http.build()
    }

    @Bean
    fun jwtDecoder(): ReactiveJwtDecoder {
        return ReactiveJwtDecoders.fromIssuerLocation("http://localhost:8081/auth/realms/just-annotate")
    }

}