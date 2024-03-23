package pl.itj.dev.justannotatebackend.domain

import java.time.LocalDateTime

data class Project(
        val id: String,
        val name: String,
        val description: String?,
        val type: ProjectType,
        val owner: String,
        val createdAt: LocalDateTime,
        val lastModifiedDate: LocalDateTime,
        val labels: Set<Label>
)
