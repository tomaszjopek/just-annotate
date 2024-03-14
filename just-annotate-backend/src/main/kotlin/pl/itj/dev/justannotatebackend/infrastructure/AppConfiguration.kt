package pl.itj.dev.justannotatebackend.infrastructure

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.itj.dev.justannotatebackend.domain.services.CsvFileImporter
import java.time.Clock

@Configuration
class AppConfiguration {

    @Bean
    fun clockSupplier(): Clock {
        return Clock.systemDefaultZone()
    }

    @Bean
    fun csvFileImporter(): CsvFileImporter {
        return CsvFileImporter()
    }

}