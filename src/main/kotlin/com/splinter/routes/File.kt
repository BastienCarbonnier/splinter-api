import com.splinter.engine.merger.constructResponseFile
import com.splinter.engine.merger.findCommonKeysBetweenFiles
import com.splinter.engine.merger.isMergeFileContainAllKeys
import com.splinter.engine.merger.removeCommonKeysFromFiles
import com.splinter.engine.parser.decodeJsonFileFromString
import com.splinter.model.*
import com.splinter.model.enums.Brand
import com.splinter.model.enums.Language
import com.splinter.model.enums.Province
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.getResultingFile() {
    get("/") {
        val s = "{\"name\": \"en_NL.i18n\", \"json\":{\"test_key\":\"test value\",\"test_key_object\":{\"test_key\":\"test value\"}}}"
        call.respond(decodeJsonFileFromString(s))
    }
}

fun Route.getDuplicateKeyFromFiles() {
    post("/file") {
        val data = call.receive<PostRequest>()
        val responseMap = findCommonKeysBetweenFiles(data.files)
        val responseJson = constructResponseFile(UUID.randomUUID(),"results.json", responseMap)

        call.respond(responseJson)
    }
}

fun Route.removeAndGetCommonKeysFromFiles() {
    post("/files") {
        val request = call.receive<PostRequest>()
        call.respond(removeCommonKeysFromFiles(request.files))
    }
}

fun Route.mergeFileContainAllKeys() {
    post("/files/validation") {
        val request = call.receive<PostValidationRequest>()
        call.respond(PostValidationResponse(isMergeFileContainAllKeys(request.files, request.referenceFile)))
    }
}

fun Route.removeAndGetCommonKeysFromFilesAll() {
    post("/files/all-files") {
        val request = call.receive<PostRequest>()
        val response = PostResponseAllFiles()
        val allFiles = request.files

        for (lang in Language.entries) {
            val filteredFilesByLang: List<JsonFile> = allFiles.filter { it.name.contains(lang.label) }
            val commonKeysByLang = removeCommonKeysFromFiles(filteredFilesByLang)
            response[lang] = commonKeysByLang.mergedFile
            for (brand in Brand.entries) {
                if (response.brands[brand] == null) response.brands[brand] = BrandFiles()

                val filteredFilesByBrand = commonKeysByLang.files.filter { it.name.contains(brand.label) }
                val commonKeysByBrand = removeCommonKeysFromFiles(filteredFilesByBrand)
                if (commonKeysByBrand.mergedFile.json.isNotEmpty()) {
                    response.brands[brand]?.set(lang, commonKeysByBrand.mergedFile)
                }

                for (file in commonKeysByBrand.files) {
                    if (file.json.isNotEmpty()) {
                        val province = Province.from(getProvinceFromFileName(file.name, brand, lang))
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
        call.respond(response)
    }
}

fun getProvinceFromFileName(name: String, brand: Brand, lang: Language): String {
    return name.replace(brand.label, "")
        .replace("${lang.label}_CA", "")
        .replace("_", "")
        .replace(".json", "")
}