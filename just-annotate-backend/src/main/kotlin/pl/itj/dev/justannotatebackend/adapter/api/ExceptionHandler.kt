package pl.itj.dev.justannotatebackend.adapter.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import pl.itj.dev.justannotatebackend.adapter.api.exceptions.ObjectNotFound

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(ObjectNotFound::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(ex: ObjectNotFound): ErrorResponse {
        return ErrorResponse(
                type = "about:blank",
                title = "Object not found"
        )
    }

}

data class ErrorResponse(
        val type: String,
        val title: String,
        val status: Int? = null,
        val detail: String? = null,
        val instance: String? = null
)
