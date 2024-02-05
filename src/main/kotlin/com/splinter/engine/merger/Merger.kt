package com.splinter.engine.merger

import com.splinter.model.*
import com.splinter.model.enums.BrandEnum
import com.splinter.model.enums.LanguageEnum
import com.splinter.model.enums.ProvinceEnum
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

fun isMergeFileContainAllKeys(jsons: List<JsonObject?>, referenceFile: JsonObject): Boolean {
    var mergedJson = emptyMap<String, JsonElement>()
    jsons.forEach { json -> if (json != null) mergedJson = mergedJson.plus(json.toMap()) }
    return mergedJson.toSortedMap() == referenceFile.toSortedMap()
}

fun processAllBrandFiles(allFiles: List<JsonFile>): PostResponseAllFiles {
    val response = PostResponseAllFiles()

    for (lang in LanguageEnum.entries) {
        val filteredFilesByLang: List<JsonFile> = allFiles.filter { it.name.contains(lang.label) }
        val commonKeysByLang = removeCommonKeysFromFiles(filteredFilesByLang)
        response[lang] = commonKeysByLang.mergedFile
        for (brand in BrandEnum.entries) {
            if (response.brands[brand] == null) response.brands[brand] = BrandFiles()

            val filteredFilesByBrand = commonKeysByLang.files.filter { it.name.contains(brand.label) }
            val commonKeysByBrand = removeCommonKeysFromFiles(filteredFilesByBrand)
            if (commonKeysByBrand.mergedFile.json.isNotEmpty()) {
                response.brands[brand]?.set(lang, commonKeysByBrand.mergedFile)
            }

            for (file in commonKeysByBrand.files) {
                if (file.json.isNotEmpty()) {
                    val province = ProvinceEnum.getProvinceFromFileName(file.name, brand, lang)
                    if (province != null) {
                        var provincesForBrand = response.brands[brand]?.provinces?.get(province)
                        if (provincesForBrand == null) provincesForBrand = ProvinceFiles()
                        provincesForBrand[lang] = file
                        response.brands[brand]?.provinces?.set(province, provincesForBrand)
                    }
                }
            }
        }
    }
    return response
}
