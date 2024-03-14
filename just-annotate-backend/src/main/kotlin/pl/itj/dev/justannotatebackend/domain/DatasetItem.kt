package pl.itj.dev.justannotatebackend.domain

import org.springframework.data.annotation.Id
import java.time.LocalDateTime

data class DatasetItem(
        @field:Id val id: String,
        val projectId: String,
        val text: String,
        val annotations: Map<String, String>,
        val createdBy: String,
        val createdAt: LocalDateTime
)
