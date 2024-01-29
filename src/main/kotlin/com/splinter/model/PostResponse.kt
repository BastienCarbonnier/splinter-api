package com.splinter.model

import kotlinx.serialization.Serializable

@Serializable
data class PostResponse(
    val mergedFile: JsonFile,
    val files: List<JsonFile>
)