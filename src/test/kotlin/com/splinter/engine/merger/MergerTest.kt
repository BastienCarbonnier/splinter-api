package com.splinter.engine.merger

import com.splinter.model.JsonFile
import kotlinx.serialization.json.*
import org.junit.Test
import java.util.*
import kotlin.properties.Delegates
import kotlin.test.BeforeTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MergerTest {

    var jsonFile1: JsonFile by Delegates.notNull()
    var jsonFile2: JsonFile by Delegates.notNull()
    var jsonFile3: JsonFile by Delegates.notNull()

    var jsonFile4: JsonFile by Delegates.notNull()

    @BeforeTest
    fun setup() {
        val jsonObject1 = buildJsonObject {
            put("var_1", "value 1")
            put("var_2", "value 2")
        }
        jsonFile1 = JsonFile(UUID.randomUUID(),"test_1", jsonObject1)

        val jsonObject2 = buildJsonObject {
            put("var_1", "value 1")
            put("var_5", "value 5")
        }
        jsonFile2 = JsonFile(UUID.randomUUID(),"test_2", jsonObject2)

        val jsonObject3 = buildJsonObject {
            put("var_3", "value 3")
            put("var_4", "value 4")
        }
        jsonFile3 = JsonFile(UUID.randomUUID(),"test_2", jsonObject3)

        val jsonObject4 = buildJsonObject {
            put("var_1", "value 1")
            put("var_4", "value 4")
        }
        jsonFile4 = JsonFile(UUID.randomUUID(),   "test_3", jsonObject4)
    }

    @Test
    fun testGetDuplicateKeys() {
        val duplicateKeys = mutableMapOf<String, JsonElement>()
        findCommonKeysBetweenTwoFiles(jsonFile1, jsonFile1, duplicateKeys, true)
        assertEquals(duplicateKeys, mutableMapOf<String, JsonElement>(Pair("var_1", JsonPrimitive("value 1")), Pair("var_2", JsonPrimitive("value 2"))))
    }

    @Test
    fun getDuplicateKeysAny() {
        val duplicateKeys = mutableMapOf<String, JsonElement>()
        findCommonKeysBetweenTwoFiles(jsonFile1, jsonFile3, duplicateKeys, true)
        assertEquals(duplicateKeys, emptyMap())
    }

    @Test
    fun getDuplicateKeysOneElementInCommon() {
        val duplicateKeys = mutableMapOf<String, JsonElement>()
        findCommonKeysBetweenTwoFiles(jsonFile1, jsonFile4, duplicateKeys, true)
        assertEquals(duplicateKeys.size, 1)
    }

    @Test
    fun shouldFindTwoDuplicatesKey() {
        val duplicateKeys = findCommonKeysBetweenFiles(listOf(jsonFile1, jsonFile1))
        assertEquals(duplicateKeys.size, 2)
    }

    @Test
    fun shouldNotFindKeysBetweenFilesIfDifferentValues() {
        val sameJsonKey = "var_1"
        val sameJsonValue = "value 1"
        val jsonObject1 = buildJsonObject {
            put(sameJsonKey, sameJsonValue)
            put("var_5", "value 5")
        }
        jsonFile1 = JsonFile(UUID.randomUUID(),"test_1", jsonObject1)

        val jsonObject2 = buildJsonObject {
            put(sameJsonKey, sameJsonValue)
            put("var_4", "value 4")
        }
        jsonFile2 = JsonFile(UUID.randomUUID(),"test_2", jsonObject2)

        val jsonObject3 = buildJsonObject {
            put(sameJsonKey, "not same key")
            put("var_3", "value 3")
        }
        jsonFile3 = JsonFile(UUID.randomUUID(),   "test_3", jsonObject3)
        val duplicateKeys = findCommonKeysBetweenFiles(listOf(jsonFile1, jsonFile2, jsonFile3))
        assertEquals(duplicateKeys.size, 0)
    }

    @Test
    fun shouldFindOneKeysBetweenThreeFiles() {
        val sameJsonKey = "var_1"
        val sameJsonValue = "value 1"
        val jsonObject1 = buildJsonObject {
            put(sameJsonKey, sameJsonValue)
            put("var_5", "value 5")
        }
        jsonFile1 = JsonFile(UUID.randomUUID(),"test_1", jsonObject1)

        val jsonObject2 = buildJsonObject {
            put(sameJsonKey, sameJsonValue)
            put("var_4", "value 4")
        }
        jsonFile2 = JsonFile(UUID.randomUUID(),"test_2", jsonObject2)

        val jsonObject3 = buildJsonObject {
            put(sameJsonKey, sameJsonValue)
            put("var_3", "value 3")
        }
        jsonFile3 = JsonFile(UUID.randomUUID(),   "test_3", jsonObject3)
        val duplicateKeys = findCommonKeysBetweenFiles(listOf(jsonFile1, jsonFile2, jsonFile3))
        assertEquals(duplicateKeys.size, 1)
        assertContains(duplicateKeys, sameJsonKey, sameJsonValue)
    }

    @Test
    fun shouldFindNoKeysBetweenThreeFiles() {
        val sameJsonKey = "var_1"
        val sameJsonValue = "value 1"
        val jsonObject1 = buildJsonObject {
            put(sameJsonKey, sameJsonValue)
            put("var_5", "value 5")
        }
        jsonFile1 = JsonFile(UUID.randomUUID(),"test_1", jsonObject1)

        val jsonObject2 = buildJsonObject {
            put(sameJsonKey, sameJsonValue)
            put("var_4", "value 4")
        }
        jsonFile2 = JsonFile(UUID.randomUUID(),"test_2", jsonObject2)

        val jsonObject3 = buildJsonObject {
            put("var_9", "value 9")
            put("var_3", "value 3")
        }
        jsonFile3 = JsonFile(UUID.randomUUID(),   "test_3", jsonObject3)
        val duplicateKeys = findCommonKeysBetweenFiles(listOf(jsonFile1, jsonFile2, jsonFile3))
        assertEquals(duplicateKeys.size, 0)
        assertNull(duplicateKeys[sameJsonKey])
    }
}