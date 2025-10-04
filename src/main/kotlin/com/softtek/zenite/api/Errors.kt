package com.softtek.zenite.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFoundException(msg: String = "not found") : RuntimeException(msg)

@ResponseStatus(HttpStatus.FORBIDDEN)
class ForbiddenException : RuntimeException("forbidden")

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadRequestException(msg: String = "bad request") : RuntimeException(msg)