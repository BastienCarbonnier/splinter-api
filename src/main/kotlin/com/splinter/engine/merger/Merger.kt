package com.splinter.engine.merger

import com.splinter.model.JsonFile
import kotlinx.serialization.json.*

fun findDuplicateKeysBetweenFiles(jsonFiles: List<JsonFile>): Map<String, String> {
    return findExistingKeysBetweenTwoFiles(jsonFiles[0], jsonFiles[1])
}

fun findExistingKeysBetweenTwoFiles(file1: JsonFile, file2: JsonFile): Map<String, String> {
    val duplicateKeys = file1.json.keys.intersect(file2.json.keys)
    var sameKeyAndValue = emptyMap<String, String>()
    duplicateKeys.forEach {
        val value1 = file1.json[it]
        val value2 = file2.json[it]
        if (value1 != null && value2 != null) {
            val valueFile1 = Json.decodeFromJsonElement<String>(value1)
            val valueFile2 = Json.decodeFromJsonElement<String>(value2)
            if (valueFile1 == valueFile2) sameKeyAndValue = sameKeyAndValue.plus(Pair(it, valueFile1))
        }
    }
    return sameKeyAndValue
}

fun constructResponseFile(): JsonFile {
    val map: Map<String, JsonElement> = emptyMap()
    return JsonFile("", JsonObject(map))
}