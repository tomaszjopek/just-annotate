package pl.itj.dev.justannotatebackend.adapter.mongo

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import pl.itj.dev.justannotatebackend.domain.ProjectType
import java.time.LocalDateTime

@Document(collection = "projects")
data class ProjectDocument(
        @Id
        val id: String,
        val name: String,
        val description: String?,
        val type: String,
        val owner: String,
        val createdAt: LocalDateTime,
        val lastModifiedDate: LocalDateTime
)
