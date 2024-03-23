package pl.itj.dev.justannotatebackend.domain

import java.time.LocalDateTime

data class DatasetItem(
        val id: String,
        val projectId: String,
        val text: String,
        val annotations: List<Annotation>,
        val createdBy: String,
        val createdAt: LocalDateTime,
        val originalFilename: String
)
