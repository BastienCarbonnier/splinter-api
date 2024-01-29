package com.splinter.engine.merger

import com.splinter.model.JsonFile
import com.splinter.model.PostResponse
import kotlinx.serialization.json.*
import java.util.*


fun findCommonKeysBetweenFiles(jsonFiles: List<JsonFile>): Map<String, JsonElement> {
    var commonKeyAndValue = mutableMapOf<String, JsonElement>()
    for (i in 0..<jsonFiles.size-1) {
        val file1 = jsonFiles[i]
        val file2 = jsonFiles[i+1]
        val tempKeyAndValue = findCommonKeysBetweenTwoFiles(file1, file2)
        if (i>0) {
            val commonKeys = commonKeyAndValue.keys.intersect(tempKeyAndValue.keys)
            commonKeyAndValue = commonKeyAndValue.filterKeys { key -> commonKeys.contains(key) }.toMutableMap()
            tempKeyAndValue.forEach { (key, value) ->
                if (commonKeyAndValue[key] != value) {
                    commonKeyAndValue.remove(key)
                }
            }

        } else {
            commonKeyAndValue.putAll(tempKeyAndValue)
        }
    }
    return commonKeyAndValue.toMap()
}

fun findCommonKeysBetweenTwoFiles(
    file1: JsonFile,
    file2: JsonFile
): Map<String, JsonElement> {
    val commonKeys = file1.json.keys.intersect(file2.json.keys)
    val commonKeyAndValue = mutableMapOf<String, JsonElement>()
    commonKeys.forEach {
        val valueFile1 = file1.json[it]
        val valueFile2 = file2.json[it]
        if (valueFile1 != null && valueFile2 != null && valueFile1 == valueFile2) {
            commonKeyAndValue[it] = valueFile1
        }
    }
    return commonKeyAndValue
}

fun removeCommonKeysFromFiles(files: List<JsonFile>): PostResponse {
    val commonKeys = findCommonKeysBetweenFiles(files)
    val updatedFiles = removeKeysFromFiles(files, commonKeys.keys)
    val mergedFile = constructResponseFile(UUID.randomUUID(),"common_keys_and_values.json", commonKeys)
    return PostResponse(mergedFile, updatedFiles)
}

fun removeKeysFromFiles(files: List<JsonFile>, keys: Set<String>): List<JsonFile> {
    println("removeKeysFromFiles")
    println(keys.toString())
    var resultFiles = emptyList<JsonFile>()
    files.forEach {
        val json = it.json
        val updatedJson = json.filterKeys { key -> !keys.contains(key) }
        println("fileName: ${it.name}")
        println("updatedJson")
        println(updatedJson.toString())
        val updatedFile = JsonFile(it.id, it.name, JsonObject(updatedJson))
        resultFiles = resultFiles.plus(updatedFile)
    }
    return resultFiles
}

fun constructResponseFile(id: UUID, name: String, map: Map<String, JsonElement>): JsonFile {
    return JsonFile(id, name, JsonObject(map))
}