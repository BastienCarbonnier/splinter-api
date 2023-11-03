package com.splinter.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class JsonFile(
    val name: String,
    val json: JsonObject //can be any JSON object
)