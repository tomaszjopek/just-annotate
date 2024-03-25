package pl.itj.dev.justannotatebackend.adapter.api.projects

import jakarta.validation.constraints.NotEmpty

data class ProjectCreateRequest(
        @field:NotEmpty
        val name: String,
        val description: String?,
        val type: String,
        val labels: Set<LabelJson>
)
