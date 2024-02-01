package com.splinter.model

import kotlinx.serialization.Serializable

@Serializable
data class PostValidationResponse(
    val isValid: Boolean
)