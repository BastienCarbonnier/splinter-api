package com.splinter.engine.merger

import com.splinter.model.JsonFile
import kotlinx.serialization.json.*
import java.util.UUID

fun findDuplicateKeysBetweenFiles(jsonFiles: List<JsonFile>): Map<String, JsonElement> {
    val duplicateKeyAndValue = mutableMapOf<String, JsonElement>()
    for (i in 0..<jsonFiles.size-1) {
        findExistingKeysBetweenTwoFiles(jsonFiles[i], jsonFiles[i+1], duplicateKeyAndValue)
    }
    return duplicateKeyAndValue.toMap()
}

fun findExistingKeysBetweenTwoFiles(
    file1: JsonFile,
    file2: JsonFile,
    duplicateKeyAndValue: MutableMap<String, JsonElement>
) {
    val duplicateKeys = file1.json.keys.intersect(file2.json.keys)
    duplicateKeyAndValue.forEach { (key, value) ->
        if (!file2.json.contains(key) || file2.json[key] != value) {
            duplicateKeyAndValue.remove(key)
        }
    }
    duplicateKeys.forEach {
        file1.json[it]?.let { value1 ->
            val value2 = file2.json[it]
            if (value1 == value2) duplicateKeyAndValue[it] = value1
        }
    }
}

fun constructResponseFile(id: UUID, name: String, map: Map<String, JsonElement>): JsonFile {
    return JsonFile(id, name, JsonObject(map))
}