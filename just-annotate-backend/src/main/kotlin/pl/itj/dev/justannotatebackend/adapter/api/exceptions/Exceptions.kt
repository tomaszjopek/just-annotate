package pl.itj.dev.justannotatebackend.adapter.api.exceptions

sealed class ApiException(msg: String) : RuntimeException(msg)

class ObjectNotFound(msg: String) : ApiException(msg)