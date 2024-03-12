package pl.itj.dev.justannotatebackend.adapter.api

import java.time.LocalDateTime

data class ProjectResponse(
        val id: String,
        val name: String,
        val description: String?,
        val type: String,
        val owner: String,
        val createdAt: LocalDateTime
)
