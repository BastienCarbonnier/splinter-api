package com.splinter.engine.merger

import com.splinter.model.JsonFile
import kotlinx.serialization.json.*
import java.util.UUID

fun findDuplicateKeysBetweenFiles(jsonFiles: List<JsonFile>): Map<String, JsonElement> {
    val duplicateKeyAndValue = mutableMapOf<String, JsonElement>()
    for (i in 0..<jsonFiles.size-1) {
        findExistingKeysBetweenTwoFiles(jsonFiles[i], jsonFiles[i+1], duplicateKeyAndValue, i<1)
    }
    return duplicateKeyAndValue.toMap()
}

fun findExistingKeysBetweenTwoFiles(
    file1: JsonFile,
    file2: JsonFile,
    duplicateKeyAndValue: MutableMap<String, JsonElement>,
    isFirstTwoFile: Boolean
) {
    val duplicateKeys = file1.json.keys.intersect(file2.json.keys)

    if (duplicateKeys.isEmpty()) duplicateKeyAndValue.clear()

    duplicateKeys.forEach {
        val valueFile1 = file1.json[it]
        val valueFile2 = file2.json[it]
        if (isFirstTwoFile) {
            if (valueFile1 != null && valueFile2 != null && valueFile1 == valueFile2) {
                duplicateKeyAndValue[it] = valueFile1
            }
        } else {
            if (duplicateKeyAndValue.containsKey(it)) {
                val isValueEqual = valueFile1 == valueFile2 && valueFile1 == duplicateKeyAndValue[it]
                if (!isValueEqual) {
                    duplicateKeyAndValue.remove(it)
                }
            }
        }

    }

}

fun constructResponseFile(id: UUID, name: String, map: Map<String, JsonElement>): JsonFile {
    return JsonFile(id, name, JsonObject(map))
}