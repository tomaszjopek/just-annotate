package pl.itj.dev.justannotatebackend.infrastructure.security

import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

fun JwtAuthenticationToken.username(): String {
    val jwt = this.principal as Jwt
    return jwt.claims["preferred_username"] as String? ?: throw UsernameNotPresent()
}