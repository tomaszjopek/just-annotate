package pl.itj.dev.justannotatebackend.domain

import java.time.LocalDateTime

data class Annotation(
        val label: String,
        val annotator: String,
        val createdAt: LocalDateTime
)
