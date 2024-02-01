package com.splinter.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class PostValidationRequest(
    val files: List<JsonObject>,
    val referenceFile: JsonObject
)