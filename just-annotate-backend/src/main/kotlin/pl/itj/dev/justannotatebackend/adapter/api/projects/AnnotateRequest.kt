package pl.itj.dev.justannotatebackend.adapter.api.projects

import jakarta.validation.constraints.NotNull

data class AnnotateRequest(@field:NotNull val label: String)
