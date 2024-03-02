package pl.itj.dev.justannotatebackend.adapter.api

import jakarta.validation.constraints.NotEmpty

data class ProjectCreateRequest(@NotEmpty val name: String)
