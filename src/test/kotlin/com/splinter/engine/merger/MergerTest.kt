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
import kotlin.test.*

class MergerTest {

    private var jsonFile1: JsonFile by Delegates.notNull()
    private var jsonFile2: JsonFile by Delegates.notNull()
    private var jsonFile3: JsonFile by Delegates.notNull()

    private var jsonFile4: JsonFile by Delegates.notNull()

    val COUNT_BELAIR_QC = 500
    val COUNT_BELAIR_AB = 500
    val COUNT_BELAIR_ON = 500
    val COUNT_SCOTIA_ON = 500

    val COUNT_COMMON_KEYS = 300
    val COUNT_COMMON_BELAIR = 200

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
    fun shouldProcessAllBrandFilesEnglish() {
        val sameJsonKeyAll = "var_1"
        val sameJsonValueAll = "all brand"

        val sameJsonKeyBelair = "var_2"
        val sameJsonValueBelair = "only belair"

        val jsonObject1 = buildJsonObject {
            put(sameJsonKeyAll, sameJsonValueAll)
            put(sameJsonKeyBelair, sameJsonValueBelair)
            put("var_5", "only belair qc")
        }
        jsonFile1 = JsonFile(UUID.randomUUID(),"belair_en_CA_qc.json", jsonObject1)

        val jsonObject2 = buildJsonObject {
            put(sameJsonKeyAll, sameJsonValueAll)
            put(sameJsonKeyBelair, sameJsonValueBelair)
            put("var_3", "only belair ab")
        }
        jsonFile2 = JsonFile(UUID.randomUUID(),"belair_en_CA_ab.json", jsonObject2)

        val jsonObject3 = buildJsonObject {
            put(sameJsonKeyAll, sameJsonValueAll)
            put("var_3", "only scotia qc 1")
            put("var_4", "only scotia qc 2")
        }
        jsonFile3 = JsonFile(UUID.randomUUID(),   "scotia_en_CA_qc.json", jsonObject3)

        val postResponse = processAllBrandFiles(listOf(jsonFile1, jsonFile2, jsonFile3))
        val belairENQuebecJson = getJsonFileForBrandProvinceAndLang(postResponse, BrandEnum.BELAIR, ProvinceEnum.QUEBEC, LanguageEnum.ENGLISH)
        val belairENAlbertaJson = getJsonFileForBrandProvinceAndLang(postResponse, BrandEnum.BELAIR, ProvinceEnum.ALBERTA, LanguageEnum.ENGLISH)
        val scotiaENQuebecJson = getJsonFileForBrandProvinceAndLang(postResponse, BrandEnum.SCOTIA, ProvinceEnum.QUEBEC, LanguageEnum.ENGLISH)
        val belairENJson = getJsonFileForBrandAndLang(postResponse, BrandEnum.BELAIR, LanguageEnum.ENGLISH)
        val scotiaENJson = getJsonFileForBrandAndLang(postResponse, BrandEnum.SCOTIA, LanguageEnum.ENGLISH)

        // Assert common key between all files
        assertEquals( 1, postResponse.en?.json?.size)
        assertEquals(sameJsonValueAll, getJsonContentForKey(sameJsonKeyAll, postResponse.en?.json))

        // Assert common key for Belair
        assertEquals(belairENJson?.size, 1)
        assertEquals(sameJsonValueBelair, getJsonContentForKey(sameJsonKeyBelair, belairENJson))

        assertEquals( 1, belairENQuebecJson?.size)
        assertEquals( "only belair qc", getJsonContentForKey("var_5", belairENQuebecJson))
        assertEquals( 1, belairENAlbertaJson?.size)
        assertEquals( "only belair ab", getJsonContentForKey("var_3", belairENAlbertaJson))
        assertEquals(2, scotiaENQuebecJson?.size)
        assertEquals( "only scotia qc 1", getJsonContentForKey("var_3", scotiaENQuebecJson))
        assertEquals( "only scotia qc 2", getJsonContentForKey("var_4", scotiaENQuebecJson))

        assertNull(getJsonFileForBrandAndLang(postResponse, BrandEnum.BELAIR, LanguageEnum.FRENCH))
        assertNull(scotiaENJson)
        checkEmptyBrand(postResponse, listOf(BrandEnum.BNC, BrandEnum.INTACT, BrandEnum.SERVUS))

        val belairEnQuebecJsonFiles = listOf(belairENQuebecJson, belairENJson, postResponse.en?.json)
        assertEquals(true, isMergeFileContainAllKeys(belairEnQuebecJsonFiles, jsonFile1.json))
    }

    @Test
    fun shouldProcessAllBrandFilesFrench() {
        val sameJsonKeyAll = "var_1"
        val sameJsonValueAll = "all brand"

        val sameJsonKeyBelair = "var_2"
        val sameJsonValueBelair = "only belair"

        val jsonObject1 = buildJsonObject {
            put(sameJsonKeyAll, sameJsonValueAll)
            put(sameJsonKeyBelair, sameJsonValueBelair)
            put("var_5", "only belair qc")
        }
        jsonFile1 = JsonFile(UUID.randomUUID(),"belair_fr_CA_qc.json", jsonObject1)

        val jsonObject2 = buildJsonObject {
            put(sameJsonKeyAll, sameJsonValueAll)
            put(sameJsonKeyBelair, sameJsonValueBelair)
            put("var_3", "only belair ab")
        }
        jsonFile2 = JsonFile(UUID.randomUUID(),"belair_fr_CA_ab.json", jsonObject2)

        val jsonObject3 = buildJsonObject {
            put(sameJsonKeyAll, sameJsonValueAll)
            put("var_3", "only scotia qc 1")
            put("var_4", "only scotia qc 2")
        }
        jsonFile3 = JsonFile(UUID.randomUUID(),   "scotia_fr_CA_qc.json", jsonObject3)

        val postResponse = processAllBrandFiles(listOf(jsonFile1, jsonFile2, jsonFile3))
        val belairFRQuebecJson = getJsonFileForBrandProvinceAndLang(postResponse, BrandEnum.BELAIR, ProvinceEnum.QUEBEC, LanguageEnum.FRENCH)
        val belairFRAlbertaJson = getJsonFileForBrandProvinceAndLang(postResponse, BrandEnum.BELAIR, ProvinceEnum.ALBERTA, LanguageEnum.FRENCH)
        val scotiaFRQuebecJson = getJsonFileForBrandProvinceAndLang(postResponse, BrandEnum.SCOTIA, ProvinceEnum.QUEBEC, LanguageEnum.FRENCH)
        val belairFRJson = getJsonFileForBrandAndLang(postResponse, BrandEnum.BELAIR, LanguageEnum.FRENCH)

        // Assert common key between all files
        assertEquals( 1, postResponse.fr?.json?.size)
        assertEquals(sameJsonValueAll, getJsonContentForKey(sameJsonKeyAll, postResponse.fr?.json))

        // Assert common key for Belair
        assertEquals(belairFRJson?.size, 1)
        assertEquals(sameJsonValueBelair, getJsonContentForKey(sameJsonKeyBelair, belairFRJson))

        assertEquals( 1, belairFRQuebecJson?.size)
        assertEquals( "only belair qc", getJsonContentForKey("var_5", belairFRQuebecJson))
        assertEquals( 1, belairFRAlbertaJson?.size)
        assertEquals( "only belair ab", getJsonContentForKey("var_3", belairFRAlbertaJson))
        assertEquals(2, scotiaFRQuebecJson?.size)
        assertEquals( "only scotia qc 1", getJsonContentForKey("var_3", scotiaFRQuebecJson))
        assertEquals( "only scotia qc 2", getJsonContentForKey("var_4", scotiaFRQuebecJson))

        assertNull(getJsonFileForBrandAndLang(postResponse, BrandEnum.BELAIR, LanguageEnum.ENGLISH))

        checkEmptyBrand(postResponse, listOf(BrandEnum.BNC, BrandEnum.INTACT, BrandEnum.SERVUS))

        val belairFRQuebecJsonFiles = listOf(belairFRQuebecJson, belairFRJson, postResponse.fr?.json)
        assertEquals(true, isMergeFileContainAllKeys(belairFRQuebecJsonFiles, jsonFile1.json))
    }

    fun generateListOfJsonObject(numberOfObject: Int, makeRandomData: Boolean): Map<String, JsonPrimitive> {
        val jsonElementsMap = emptyMap<String, JsonPrimitive>().toMutableMap()
        for (i in 1..numberOfObject) {
            val id = if (makeRandomData) UUID.randomUUID() else i
            jsonElementsMap["key-${id}"] = JsonPrimitive("value-${id}")
        }
        return jsonElementsMap
    }

    @Test
    fun shouldProcessAllBrandFilesChargeTest() {
        val commonKeysValues = generateListOfJsonObject(COUNT_COMMON_KEYS, false)
        val commonKeysValuesBelair = generateListOfJsonObject(COUNT_COMMON_BELAIR, true)

        val belairENQCJsonObjects = commonKeysValues
            .plus(commonKeysValuesBelair)
            .plus(generateListOfJsonObject(COUNT_BELAIR_QC, true))
        val belairENQC = JsonFile(UUID.randomUUID(),"belair_en_CA_qc.json", JsonObject(belairENQCJsonObjects))
        
        val belairENABJsonObjects = commonKeysValues
            .plus(commonKeysValuesBelair)
            .plus(generateListOfJsonObject(COUNT_BELAIR_AB, true))
        val belairENAB = JsonFile(UUID.randomUUID(),"belair_en_CA_ab.json", JsonObject(belairENABJsonObjects))

        val belairENONJsonObjects = commonKeysValues
            .plus(commonKeysValuesBelair)
            .plus(generateListOfJsonObject(COUNT_BELAIR_ON, true))
        val belairENON = JsonFile(UUID.randomUUID(),"belair_en_CA_on.json", JsonObject(belairENONJsonObjects))

        val scotiaENONJsonObjects = commonKeysValues.plus(generateListOfJsonObject(COUNT_SCOTIA_ON, true))
        val scotiaENON = JsonFile(UUID.randomUUID(),"scotia_en_CA_on.json", JsonObject(scotiaENONJsonObjects))

        val belairFRQCJsonObjects = commonKeysValues
            .plus(commonKeysValuesBelair)
            .plus(generateListOfJsonObject(COUNT_BELAIR_QC, true))
        val belairFRQC = JsonFile(UUID.randomUUID(),"belair_fr_CA_qc.json", JsonObject(belairFRQCJsonObjects))

        val belairFRABJsonObjects = commonKeysValues
            .plus(commonKeysValuesBelair)
            .plus(generateListOfJsonObject(COUNT_BELAIR_AB, true))
        val belairFRAB = JsonFile(UUID.randomUUID(),"belair_fr_CA_ab.json", JsonObject(belairFRABJsonObjects))

        val belairFRONJsonObjects = commonKeysValues
            .plus(commonKeysValuesBelair)
            .plus(generateListOfJsonObject(COUNT_BELAIR_ON, true))
        val belairFRON = JsonFile(UUID.randomUUID(),"belair_fr_CA_on.json", JsonObject(belairFRONJsonObjects))

        val scotiaFRONJsonObjects = commonKeysValues.plus(generateListOfJsonObject(COUNT_SCOTIA_ON, true))
        val scotiaFRON = JsonFile(UUID.randomUUID(),"scotia_fr_CA_on.json", JsonObject(scotiaFRONJsonObjects))

        val postResponse = processAllBrandFiles(listOf(
            belairENQC, belairENAB, belairENON, scotiaENON,
            belairFRQC, belairFRAB, belairFRON, scotiaFRON
        ))
        testPostResponseByLang(postResponse, LanguageEnum.ENGLISH, commonKeysValues, belairENQCJsonObjects, scotiaENONJsonObjects)
        testPostResponseByLang(postResponse, LanguageEnum.FRENCH, commonKeysValues, belairFRQCJsonObjects, scotiaFRONJsonObjects)

        checkEmptyBrand(postResponse, listOf(BrandEnum.BNC, BrandEnum.INTACT, BrandEnum.SERVUS))
    }
    
    private fun testPostResponseByLang(postResponse: PostResponseAllFiles, 
                                       lang: LanguageEnum, 
                                       commonKeysValues: Map<String, JsonPrimitive>,
                                       belairQCJsonObjects: Map<String, JsonPrimitive>,
                                       scotiaONJsonObjects: Map<String, JsonPrimitive>) {
        val belairQuebecJson = getJsonFileForBrandProvinceAndLang(postResponse, BrandEnum.BELAIR, ProvinceEnum.QUEBEC, lang)
        val belairAlbertaJson = getJsonFileForBrandProvinceAndLang(postResponse, BrandEnum.BELAIR, ProvinceEnum.ALBERTA, lang)
        val belairOntarioJson = getJsonFileForBrandProvinceAndLang(postResponse, BrandEnum.BELAIR, ProvinceEnum.ONTARIO, lang)

        val scotiaOntarioJson = getJsonFileForBrandProvinceAndLang(postResponse, BrandEnum.SCOTIA, ProvinceEnum.ONTARIO, lang)

        val belairJson = getJsonFileForBrandAndLang(postResponse, BrandEnum.BELAIR, lang)
        val allJson = getJsonFileForLang(postResponse, lang)

        // Assert common key between all files
        assertNotNull(allJson)
        assertEquals(COUNT_COMMON_KEYS, allJson.size)
        assert(allJson.all { commonKeysValues[it.key] == it.value })

        assertEquals(COUNT_BELAIR_QC, belairQuebecJson?.size)
        assertEquals(COUNT_BELAIR_AB, belairAlbertaJson?.size)
        assertEquals(COUNT_BELAIR_ON, belairOntarioJson?.size)
        assertEquals(COUNT_SCOTIA_ON, scotiaOntarioJson?.size)
        assertNotNull(belairJson)
        assertNotNull(belairQuebecJson)
        assertNotNull(scotiaOntarioJson)

        assertEquals(COUNT_COMMON_BELAIR, belairJson.size)
        assert(allJson.plus(belairJson.toMap()).plus(belairQuebecJson).all { belairQCJsonObjects[it.key] == it.value })
        assert(allJson.plus(scotiaOntarioJson.toMap()).all { scotiaONJsonObjects[it.key] == it.value })
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

    private fun getJsonFileForLang(postResponse: PostResponseAllFiles, lang: LanguageEnum): JsonObject? {
        return postResponse[lang]?.json
    }
    private fun getJsonFileForBrandAndLang(postResponse: PostResponseAllFiles, brand: BrandEnum, lang: LanguageEnum): JsonObject? {
        return postResponse.brands[brand]?.get(lang)?.json
    }

    private fun getJsonFileForBrandProvinceAndLang(postResponse: PostResponseAllFiles, brand: BrandEnum, province: ProvinceEnum, lang: LanguageEnum): JsonObject? {
        return postResponse.brands[brand]?.provinces?.get(province)?.get(lang)?.json
    }
}
