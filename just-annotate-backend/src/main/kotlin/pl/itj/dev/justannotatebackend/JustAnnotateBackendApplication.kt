package pl.itj.dev.justannotatebackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@SpringBootApplication
@EnableReactiveMongoRepositories
class JustAnnotateBackendApplication

fun main(args: Array<String>) {
    runApplication<JustAnnotateBackendApplication>(*args)
}
