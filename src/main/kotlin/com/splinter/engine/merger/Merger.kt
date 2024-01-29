package com.splinter.engine.merger

import com.splinter.model.JsonFile
import com.splinter.model.PostResponse
import kotlinx.serialization.json.*
import java.util.*


fun findCommonKeysBetweenFiles(jsonFiles: List<JsonFile>): Map<String, JsonElement> {
    val commonKeyAndValue = mutableMapOf<String, JsonElement>()
    for (i in 0..<jsonFiles.size-1) {
        findCommonKeysBetweenTwoFiles(jsonFiles[i], jsonFiles[i+1], commonKeyAndValue, i<1)
    }
    return commonKeyAndValue.toMap()
}

fun findCommonKeysBetweenTwoFiles(
    file1: JsonFile,
    file2: JsonFile,
    commonKeyAndValue: MutableMap<String, JsonElement>,
    isFirstTwoFile: Boolean
) {
    val commonKeys = file1.json.keys.intersect(file2.json.keys)

    if (commonKeys.isEmpty()) commonKeyAndValue.clear()

    commonKeys.forEach {
        val valueFile1 = file1.json[it]
        val valueFile2 = file2.json[it]
        if (isFirstTwoFile) {
            if (valueFile1 != null && valueFile2 != null && valueFile1 == valueFile2) {
                commonKeyAndValue[it] = valueFile1
            }
        } else {
            if (commonKeyAndValue.containsKey(it)) {
                val isValueEqual = valueFile1 == valueFile2 && valueFile1 == commonKeyAndValue[it]
                if (!isValueEqual) {
                    commonKeyAndValue.remove(it)
                }
            }
        }
    }
}

fun removeCommonKeysFromFiles(files: List<JsonFile>): PostResponse {
    val commonKeys = findCommonKeysBetweenFiles(files)
    val updatedFiles = removeKeysFromFiles(files, commonKeys.keys)
    val mergedFile = constructResponseFile(UUID.randomUUID(),"common_keys_and_values.json", commonKeys)
    return PostResponse(mergedFile = mergedFile, updatedFiles)
}

fun removeKeysFromFiles(files: List<JsonFile>, keys: Set<String>): List<JsonFile> {
    var resultFiles = emptyList<JsonFile>()
    files.forEach {
        val json = it.json
        val updatedJson = json.filterKeys { key -> !keys.contains(key) }
        val updatedFile = JsonFile(it.id, it.name, JsonObject(updatedJson))
        resultFiles = resultFiles.plus(updatedFile)
    }
    return resultFiles
}

fun constructResponseFile(id: UUID, name: String, map: Map<String, JsonElement>): JsonFile {
    return JsonFile(id, name, JsonObject(map))
}