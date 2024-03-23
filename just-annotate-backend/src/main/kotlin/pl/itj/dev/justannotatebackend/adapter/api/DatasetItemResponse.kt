package pl.itj.dev.justannotatebackend.adapter.api

data class DatasetItemResponse(
        val id: String,
        val text: String,
        val annotations: Map<String, String>
)
