package com.splinter.engine.merger

import com.splinter.model.JsonFile
import kotlinx.serialization.json.*

fun findDuplicateKeysBetweenFiles(jsonFiles: List<JsonFile>): Map<String, JsonPrimitive> {
    var sameKeyAndValue = emptyMap<String, JsonPrimitive>()
    for (i in 0..<jsonFiles.size-1) {
        sameKeyAndValue = sameKeyAndValue.plus(findExistingKeysBetweenTwoFiles(jsonFiles[i], jsonFiles[i+1]))
    }
    return sameKeyAndValue
}

fun findExistingKeysBetweenTwoFiles(file1: JsonFile, file2: JsonFile): Map<String, JsonPrimitive> {
    val duplicateKeys = file1.json.keys.intersect(file2.json.keys)
    var sameKeyAndValue = emptyMap<String, JsonPrimitive>()
    duplicateKeys.forEach {
        val value1 = file1.json[it]
        val value2 = file2.json[it]
        if (value1 != null && value2 != null) {
            val valueFile1 = Json.decodeFromJsonElement<String>(value1)
            val valueFile2 = Json.decodeFromJsonElement<String>(value2)
            if (valueFile1 == valueFile2) sameKeyAndValue = sameKeyAndValue.plus(Pair(it, JsonPrimitive(valueFile1)))
        }
    }
    return sameKeyAndValue
}

fun constructResponseFile(id: String, name: String, map: Map<String, JsonElement>): JsonFile {
    return JsonFile(id, name, JsonObject(map))
}