package pl.itj.dev.justannotatebackend.domain

import java.time.LocalDateTime

data class DatasetItem(
        val id: String,
        val projectId: String,
        val text: String,
        val annotations: Map<String, String>,
        val createdBy: String,
        val createdAt: LocalDateTime,
        val originalFileName: String
)
