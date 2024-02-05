package com.splinter.engine.merger

import com.splinter.model.JsonFile
import com.splinter.model.PostResponseAllFiles
import com.splinter.model.enums.BrandEnum
import com.splinter.model.enums.LanguageEnum
import com.splinter.model.enums.ProvinceEnum
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
        val duplicateKeys = findCommonKeysBetweenTwoFiles(jsonFile1, jsonFile1)
        assertEquals(duplicateKeys, mutableMapOf<String, JsonElement>(Pair("var_1", JsonPrimitive("value 1")), Pair("var_2", JsonPrimitive("value 2"))))
    }

    @Test
    fun getDuplicateKeysAny() {
        val duplicateKeys = findCommonKeysBetweenTwoFiles(jsonFile1, jsonFile3)
        assertEquals(duplicateKeys, emptyMap())
    }

    @Test
    fun getDuplicateKeysOneElementInCommon() {
        val duplicateKeys = findCommonKeysBetweenTwoFiles(jsonFile1, jsonFile4)
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

    @Test
    fun shouldFindOneKeysBetweenThreeDifferentFiles() {
        val sameJsonKey = "var_1"
        val sameJsonValue = "value 1"
        val jsonObject1 = buildJsonObject {
            put(sameJsonKey, sameJsonValue)
            put("var_5", "value 5")
        }
        jsonFile1 = JsonFile(UUID.randomUUID(),"test_1", jsonObject1)

        val jsonObject2 = buildJsonObject {
            put(sameJsonKey, sameJsonValue)
            put("var_3", "value 3")
        }
        jsonFile2 = JsonFile(UUID.randomUUID(),"test_2", jsonObject2)

        val jsonObject3 = buildJsonObject {
            put(sameJsonKey, sameJsonValue)
            put("var_3", "value 3")

            put("var_4", "value 4")
        }
        jsonFile3 = JsonFile(UUID.randomUUID(),   "test_3", jsonObject3)
        val duplicateKeys = findCommonKeysBetweenFiles(listOf(jsonFile1, jsonFile2, jsonFile3))
        assertEquals(duplicateKeys.size, 1)
        assertContains(duplicateKeys, sameJsonKey, sameJsonValue)
    }

    @Test
    fun shouldRemoveKeysFromFiles() {
        val sameJsonKey = "var_1"
        val sameJsonValue = "value 1"
        val jsonObject1 = buildJsonObject {
            put(sameJsonKey, sameJsonValue)
            put("var_5", "value 5")
        }
        jsonFile1 = JsonFile(UUID.randomUUID(),"test_1", jsonObject1)

        val jsonObject2 = buildJsonObject {
            put(sameJsonKey, sameJsonValue)
            put("var_3", "value 3")
        }
        jsonFile2 = JsonFile(UUID.randomUUID(),"test_2", jsonObject2)

        val jsonObject3 = buildJsonObject {
            put(sameJsonKey, sameJsonValue)
            put("var_3", "value 3")

            put("var_4", "value 4")
        }
        jsonFile3 = JsonFile(UUID.randomUUID(),   "test_3", jsonObject3)
        val files = removeKeysFromFiles(listOf(jsonFile1, jsonFile2, jsonFile3), setOf("var_1"))
        assertEquals(files.find { file -> file.name == jsonFile1.name }?.json?.size, 1)
        assertEquals(files.find { file -> file.name == jsonFile2.name }?.json?.size, 1)
        assertEquals(files.find { file -> file.name == jsonFile3.name }?.json?.size, 2)
    }

    @Test
    fun shouldRemoveCommonKeysFromFiles() {
        val sameJsonKey = "var_1"
        val sameJsonValue = "value 1"
        val jsonObject1 = buildJsonObject {
            put(sameJsonKey, sameJsonValue)
            put("var_5", "value 5")
        }
        jsonFile1 = JsonFile(UUID.randomUUID(),"test_1", jsonObject1)

        val jsonObject2 = buildJsonObject {
            put(sameJsonKey, sameJsonValue)
            put("var_3", "value 3")
        }
        jsonFile2 = JsonFile(UUID.randomUUID(),"test_2", jsonObject2)

        val jsonObject3 = buildJsonObject {
            put(sameJsonKey, sameJsonValue)
            put("var_3", "value 3")

            put("var_4", "value 4")
        }
        jsonFile3 = JsonFile(UUID.randomUUID(),   "test_3", jsonObject3)
        val postResponse = removeCommonKeysFromFiles(listOf(jsonFile1, jsonFile2, jsonFile3))
        val files = postResponse.files
        assertEquals(files.find { file -> file.name == jsonFile1.name }?.json?.size, 1)
        assertEquals(files.find { file -> file.name == jsonFile2.name }?.json?.size, 1)
        assertEquals(files.find { file -> file.name == jsonFile3.name }?.json?.size, 2)

        assertEquals(postResponse.mergedFile.json.size, 1)
    }

    @Test
    fun shouldProcessAllBrandFiles() {
        val sameJsonKey = "var_1"
        val sameJsonValue = "value 1"
        val jsonObject1 = buildJsonObject {
            put(sameJsonKey, sameJsonValue)
            put("var_5", "value 5")
        }
        jsonFile1 = JsonFile(UUID.randomUUID(),"belair_en_CA_qc.json", jsonObject1)

        val jsonObject2 = buildJsonObject {
            put(sameJsonKey, sameJsonValue)
            put("var_3", "value 3")
        }
        jsonFile2 = JsonFile(UUID.randomUUID(),"belair_en_CA_ab.json", jsonObject2)

        val jsonObject3 = buildJsonObject {
            put(sameJsonKey, sameJsonValue)
            put("var_3", "value 3")
            put("var_4", "value 4")
        }
        jsonFile3 = JsonFile(UUID.randomUUID(),   "scotia_en_CA_qc.json", jsonObject3)
        val postResponse = processAllBrandFiles(listOf(jsonFile1, jsonFile2, jsonFile3))
        val belairEnQuebecJson = getJsonFileForBrandProvinceAndLang(postResponse, BrandEnum.BELAIR, ProvinceEnum.QUEBEC, LanguageEnum.ENGLISH)
        val belairEnABJson = getJsonFileForBrandProvinceAndLang(postResponse, BrandEnum.BELAIR, ProvinceEnum.ALBERTA, LanguageEnum.ENGLISH)
        val scotiaEnQuebecJson = getJsonFileForBrandProvinceAndLang(postResponse, BrandEnum.SCOTIA, ProvinceEnum.QUEBEC, LanguageEnum.ENGLISH)

        assertEquals(postResponse.en?.json?.size, 1)
        assertEquals(sameJsonValue, getJsonContentForKey(sameJsonKey, postResponse.en?.json))
        assertEquals( 1, belairEnQuebecJson?.size)
        assertEquals( "value 5", getJsonContentForKey("var_5failed", belairEnQuebecJson))
        assertEquals( 1, belairEnABJson?.size)
        assertEquals( "value 3", getJsonContentForKey("var_3", belairEnABJson))
        assertEquals(2, scotiaEnQuebecJson?.size)
        assertEquals( "value 4", getJsonContentForKey("var_4", scotiaEnQuebecJson))
        assertEquals( "value 3", getJsonContentForKey("var_3", scotiaEnQuebecJson))

        assertNull(getJsonFileForBrandAndLang(postResponse, BrandEnum.BELAIR, LanguageEnum.ENGLISH))
        assertNull(getJsonFileForBrandAndLang(postResponse, BrandEnum.BELAIR, LanguageEnum.FRENCH))

        checkEmptyBrand(postResponse, listOf(BrandEnum.BNC, BrandEnum.INTACT, BrandEnum.SERVUS))

        val belairEnQuebecJsonFiles = listOf(belairEnQuebecJson, postResponse.en?.json)
        assertEquals(true, isMergeFileContainAllKeys(belairEnQuebecJsonFiles, jsonFile1.json))
    }

    private fun checkEmptyBrand(postResponse: PostResponseAllFiles, brandsToCheck: List<BrandEnum>) {
        for (brand in brandsToCheck) {
            assertNull(postResponse.brands[brand]?.en)
            assertNull(postResponse.brands[brand]?.fr)
            val allProvinces = listOf(
                ProvinceEnum.ALBERTA,
                ProvinceEnum.PRINCE_EDWARDS,
                ProvinceEnum.QUEBEC,
                ProvinceEnum.BRITISH_COLUMBIA,
                ProvinceEnum.NEW_BRUNSWICK,
                ProvinceEnum.NEW_FOUNDLAND,
                ProvinceEnum.NEW_SCOTLAND,
                ProvinceEnum.ONTARIO
            )
            for (province in allProvinces) {
                assertNull(postResponse.brands[brand]?.provinces?.get(province)?.en)
                assertNull(postResponse.brands[brand]?.provinces?.get(province)?.fr)
            }
        }
    }

    private fun getJsonContentForKey(key: String, jsonObject: JsonObject?): String? {
        return jsonObject?.get(key)?.jsonPrimitive?.content
    }

    private fun getJsonFileForBrandAndLang(postResponse: PostResponseAllFiles, brand: BrandEnum, lang: LanguageEnum): JsonObject? {
        return postResponse.brands[brand]?.get(lang)?.json
    }

    private fun getJsonFileForBrandProvinceAndLang(postResponse: PostResponseAllFiles, brand: BrandEnum, province: ProvinceEnum, lang: LanguageEnum): JsonObject? {
        return postResponse.brands[brand]?.provinces?.get(province)?.get(lang)?.json
    }
}