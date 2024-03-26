package pl.itj.dev.justannotatebackend.adapter.mongo

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

data class LabelDocument(
        val name: String,
        val color: String
)

@Document(collection = "projects")
data class ProjectDocument(
        @field:Id
        val id: String,
        val name: String,
        val description: String?,
        val type: String,
        val owner: String,
        val createdAt: LocalDateTime,
        val lastModifiedDate: LocalDateTime,
        val labels: Set<LabelDocument> = emptySet()
)
