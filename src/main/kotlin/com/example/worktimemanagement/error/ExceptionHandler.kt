package com.example.worktimemanagement.error

import com.example.worktimemanagement.dto.CustomResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@RestControllerAdvice
class ExceptionHandler: ResponseEntityExceptionHandler() {

    @ExceptionHandler(InvalidPasswordException::class)
    fun handleInvalidPasswordException(exception: InvalidPasswordException, request: WebRequest): ResponseEntity<Any>? {
        val headers = HttpHeaders()

        return handleExceptionInternal(
            exception,
            createErrorResponseBody(exception, request),
            headers,
            HttpStatus.BAD_REQUEST,
            request)
    }

    private fun createErrorResponseBody(exception: InvalidPasswordException, request: WebRequest): CustomResponse {
        return CustomResponse(exception.message)
    }
}