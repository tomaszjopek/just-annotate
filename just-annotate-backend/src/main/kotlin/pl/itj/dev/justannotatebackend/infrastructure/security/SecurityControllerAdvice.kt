package pl.itj.dev.justannotatebackend.infrastructure.security

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class SecurityControllerAdvice {

    @ExceptionHandler(SecurityException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun securityExceptionHandler() { }

}