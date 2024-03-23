package pl.itj.dev.justannotatebackend.adapter.mongo

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

data class AnnotationDocument(
        val label: String,
        val annotator: String,
        val createdAt: LocalDateTime
)

@Document(collection = "dataset_items")
data class DatasetItemDocument(
        @field:Id val id: String,
        val projectId: String,
        val text: String,
        val annotations: List<AnnotationDocument>,
        val createdBy: String,
        val createdAt: LocalDateTime,
        val originalFilename: String
)