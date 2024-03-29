package pl.itj.dev.justannotatebackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import pl.itj.dev.justannotatebackend.infrastructure.properties.KeycloakClientProperties

@SpringBootApplication
class JustAnnotateBackendApplication

fun main(args: Array<String>) {
    runApplication<JustAnnotateBackendApplication>(*args)
}
