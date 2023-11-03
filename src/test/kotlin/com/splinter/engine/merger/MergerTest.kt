package com.splinter.engine.merger

import com.splinter.model.JsonFile
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import org.junit.Test
import kotlin.properties.Delegates
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class MergerTest {

    var jsonFile1: JsonFile by Delegates.notNull()
    var jsonFileTotallyDifferentFrom1: JsonFile by Delegates.notNull()

    var jsonFileOneInCommonWith1: JsonFile by Delegates.notNull()

    @BeforeTest
    fun setup() {
        val jsonString1 = "{\"var_1\": \"en_NL.i18n\", \"var_2\":\"test value\"}"
        val jsonObject1 = Json.parseToJsonElement(jsonString1).jsonObject
        jsonFile1 = JsonFile("test1", jsonObject1)

        val jsonString2 = "{\"var_3\": \"en_NL.i18n\", \"var_4\":\"test value\"}"
        val jsonObject2 = Json.parseToJsonElement(jsonString2).jsonObject
        jsonFileTotallyDifferentFrom1 = JsonFile("test1", jsonObject2)


        val jsonString3 = "{\"var_1\": \"en_NL.i18n\", \"var_4\":\"test value\"}"
        val jsonObject3 = Json.parseToJsonElement(jsonString3).jsonObject
        jsonFileOneInCommonWith1 = JsonFile("test1", jsonObject3)
    }

    @Test
    fun testGetDuplicateKeys() {
        val duplicateKeys = findExistingKeysBetweenTwoFiles(jsonFile1, jsonFile1)
        assertEquals(duplicateKeys, mapOf(Pair("var_1", "en_NL.i18n"), Pair("var_2", "test value")))
    }

    @Test
    fun getDuplicateKeysAny() {
        val duplicateKeys = findExistingKeysBetweenTwoFiles(jsonFile1, jsonFileTotallyDifferentFrom1)
        assertEquals(duplicateKeys, emptyMap())
    }

    @Test
    fun getDuplicateKeysOneElementInCommon() {
        val duplicateKeys = findExistingKeysBetweenTwoFiles(jsonFile1, jsonFileOneInCommonWith1)
        assertEquals(duplicateKeys.size, 1)
    }
}