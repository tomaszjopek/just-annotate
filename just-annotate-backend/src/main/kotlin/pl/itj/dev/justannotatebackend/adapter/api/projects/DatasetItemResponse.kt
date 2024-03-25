package pl.itj.dev.justannotatebackend.adapter.api.projects

import java.time.LocalDateTime

data class AnnotationResponse(
        val label: String,
        val annotator: String,
        val createdAt: LocalDateTime
)

data class DatasetItemResponse(
        val id: String,
        val text: String,
        val annotations: List<AnnotationResponse>
)
