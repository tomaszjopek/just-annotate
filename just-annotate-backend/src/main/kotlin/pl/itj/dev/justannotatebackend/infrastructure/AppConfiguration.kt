package pl.itj.dev.justannotatebackend.infrastructure

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
class AppConfiguration {

    @Bean
    fun clockSupplier(): Clock {
        return Clock.systemDefaultZone()
    }

}