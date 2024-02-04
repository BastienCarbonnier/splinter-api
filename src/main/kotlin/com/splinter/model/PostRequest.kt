package com.splinter.model

import kotlinx.serialization.Serializable

@Serializable
data class PostRequest(
    val files: List<JsonFile>
)