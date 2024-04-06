package pl.itj.dev.justannotatebackend.adapter.api.projects

import java.time.LocalDateTime

data class ProjectResponse(
        val id: String,
        val name: String,
        val description: String?,
        val projectType: String,
        val owner: String,
        val createdAt: LocalDateTime,
        val labels: Set<LabelJson>
)
