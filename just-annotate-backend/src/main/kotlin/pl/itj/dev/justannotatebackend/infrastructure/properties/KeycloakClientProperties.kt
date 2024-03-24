package pl.itj.dev.justannotatebackend.infrastructure.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "keycloak-client")
data class KeycloakClientProperties(
        val baseUrl: String
)
