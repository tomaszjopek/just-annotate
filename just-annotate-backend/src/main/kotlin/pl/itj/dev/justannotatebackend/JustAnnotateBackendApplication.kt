package pl.itj.dev.justannotatebackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JustAnnotateBackendApplication

fun main(args: Array<String>) {
    runApplication<JustAnnotateBackendApplication>(*args)
}
