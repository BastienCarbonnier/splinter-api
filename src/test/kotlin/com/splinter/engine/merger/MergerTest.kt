package com.splinter.engine.merger

import com.splinter.model.JsonFile
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import org.junit.Test
import java.util.*
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
        jsonFile1 = JsonFile(UUID.randomUUID(),"test1", jsonObject1)

        val jsonString2 = "{\"var_3\": \"en_NL.i18n\", \"var_4\":\"test value\"}"
        val jsonObject2 = Json.parseToJsonElement(jsonString2).jsonObject
        jsonFileTotallyDifferentFrom1 = JsonFile(UUID.randomUUID(),"test1", jsonObject2)


        val jsonString3 = "{\"var_1\": \"en_NL.i18n\", \"var_4\":\"test value\"}"
        val jsonObject3 = Json.parseToJsonElement(jsonString3).jsonObject
        jsonFileOneInCommonWith1 = JsonFile(UUID.randomUUID(),   "test1", jsonObject3)
    }

    @Test
    fun testGetDuplicateKeys() {
        val duplicateKeys = mutableMapOf<String, JsonElement>()
        findExistingKeysBetweenTwoFiles(jsonFile1, jsonFile1, duplicateKeys)
        assertEquals(duplicateKeys, mutableMapOf<String, JsonElement>(Pair("var_1", JsonPrimitive("en_NL.i18n")), Pair("var_2", JsonPrimitive("test value"))))
    }

    @Test
    fun getDuplicateKeysAny() {
        val duplicateKeys = mutableMapOf<String, JsonElement>()
        findExistingKeysBetweenTwoFiles(jsonFile1, jsonFileTotallyDifferentFrom1, duplicateKeys)
        assertEquals(duplicateKeys, emptyMap())
    }

    @Test
    fun getDuplicateKeysOneElementInCommon() {
        val duplicateKeys = mutableMapOf<String, JsonElement>()
        findExistingKeysBetweenTwoFiles(jsonFile1, jsonFileOneInCommonWith1, duplicateKeys)
        assertEquals(duplicateKeys.size, 1)
    }

    @Test
    fun findDuplicateElementInListOfFiles() {
        val duplicateKeys = findDuplicateKeysBetweenFiles(listOf(jsonFile1, jsonFile1))
        assertEquals(duplicateKeys.size, 2)
    }
}