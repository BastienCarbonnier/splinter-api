package com.splinter.engine.merger

import com.splinter.model.*
import com.splinter.model.enums.BrandEnum
import com.splinter.model.enums.LanguageEnum
import com.splinter.model.enums.ProvinceEnum
import kotlinx.serialization.json.*
import java.util.*

/**
 * Method to find common keys between a list of JsonFile
 * Key and values should match
 */
fun findCommonKeysBetweenFiles(jsonFiles: List<JsonFile>): Map<String, JsonElement> {
    var commonKeyAndValue = mutableMapOf<String, JsonElement>()
    // For every files
    for (i in 0..<jsonFiles.size-1) {
        val file1 = jsonFiles[i]
        val file2 = jsonFiles[i+1]

        // We find common keys between the two current files
        val tempKeyAndValue = findCommonKeysBetweenTwoFiles(file1, file2)
        if (i>0) {
            // We check the common keys between the ones already found
            val commonKeys = commonKeyAndValue.keys.intersect(tempKeyAndValue.keys)

            // We remove from the found keys the ones that are not common with the currents files
            commonKeyAndValue = commonKeyAndValue.filterKeys { key -> commonKeys.contains(key) }.toMutableMap()

            // If value for the same keys is different we remove the key/value from common key
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

/**
 * Find common keys between two files, key and value should match
 */
fun findCommonKeysBetweenTwoFiles(
    file1: JsonFile,
    file2: JsonFile
): Map<String, JsonElement> {
    val commonKeys = file1.json.keys.intersect(file2.json.keys)
    val commonKeyAndValue = mutableMapOf<String, JsonElement>()

    // For every common key if value match we add it to common keys/value map
    commonKeys.forEach {
        val valueFile1 = file1.json[it]
        val valueFile2 = file2.json[it]
        if (valueFile1 != null && valueFile2 != null && valueFile1 == valueFile2) {
            commonKeyAndValue[it] = valueFile1
        }
    }
    return commonKeyAndValue
}

/**
 * Method to find every keys in common between a list of files and remove them from these files
 */
fun removeCommonKeysFromFiles(files: List<JsonFile>): PostResponse {
    val commonKeys = findCommonKeysBetweenFiles(files)
    val updatedFiles = removeKeysFromFiles(files, commonKeys.keys)
    val mergedFile = constructResponseFile(UUID.randomUUID(),"common_keys_and_values.json", commonKeys)
    return PostResponse(mergedFile, updatedFiles)
}

/**
 * Remove a list of keys from a list of files
 */
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

/**
 * Method to check if a list of file contains all the key/value, and no more from a reference file
 */
fun isMergeFileContainAllKeys(jsons: List<JsonObject?>, referenceFile: JsonObject): Boolean {
    var mergedJson = emptyMap<String, JsonElement>()
    jsons.forEach { json -> if (json != null) mergedJson = mergedJson.plus(json.toMap()) }
    return mergedJson.toSortedMap() == referenceFile.toSortedMap()
}

/**
 * Method to process all the list of files for all brands
 * File format : ${brand}_${lang}_CA_${province}.json
 */
fun processAllBrandFiles(allFiles: List<JsonFile>): PostResponseAllFiles {
    val response = PostResponseAllFiles()

    for (lang in LanguageEnum.entries) { // For every lang
        val filteredFilesByLang: List<JsonFile> = allFiles.filter { it.name.contains(lang.label) }

        // We found and remove every common keys for french and english
        val commonKeysByLang = removeCommonKeysFromFiles(filteredFilesByLang)
        // We add the lang file to response
        response[lang] = commonKeysByLang.mergedFile
        for (brand in BrandEnum.entries) { // For every brand
            if (response.brands[brand] == null) response.brands[brand] = BrandFiles()

            val filteredFilesByBrand = commonKeysByLang.files.filter { it.name.contains(brand.label) }

            // We found and remove every common keys for each brand
            val commonKeysByBrand = removeCommonKeysFromFiles(filteredFilesByBrand)
            if (commonKeysByBrand.mergedFile.json.isNotEmpty()) {
                // If common keys for brand is not empty we add it to reponse
                response.brands[brand]?.set(lang, commonKeysByBrand.mergedFile)
            }

            // For every province files
            for (file in commonKeysByBrand.files) {
                if (file.json.isNotEmpty()) {
                    val province = ProvinceEnum.getProvinceFromFileName(file.name, brand, lang)
                    if (province != null) {
                        var provincesForBrand = response.brands[brand]?.provinces?.get(province)
                        if (provincesForBrand == null) provincesForBrand = ProvinceFiles()
                        provincesForBrand[lang] = file

                        // If province for brand and lang is not null we add it to response
                        response.brands[brand]?.provinces?.set(province, provincesForBrand)
                    }
                }
            }
        }
    }
    return response
}
