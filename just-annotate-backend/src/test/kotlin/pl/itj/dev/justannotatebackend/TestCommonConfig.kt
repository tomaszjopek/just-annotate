package pl.itj.dev.justannotatebackend

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import java.time.*

@TestConfiguration
class TestCommonConfig {

    @Bean
    fun fixedClock(): Clock =
        Clock.fixed(
            LocalDateTime.of(2023, 10, 10, 8, 0).toInstant(ZoneOffset.ofHours(1)),
            ZoneId.of("Europe/Warsaw")
        )

}
