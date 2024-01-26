package com.splinter.engine.parser

import com.splinter.model.JsonFile
import kotlinx.serialization.json.Json

fun decodeJsonFileFromString(jsonFileString: String): JsonFile {
    return Json.decodeFromString(JsonFile.serializer(), jsonFileString)
}