import com.splinter.engine.merger.*
import com.splinter.engine.parser.decodeJsonFileFromString
import com.splinter.model.*
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

fun Route.removeAndGetCommonKeysFromAllBrandFiles() {
    post("/files/all-brand-files") {
        val request = call.receive<PostRequest>()
        call.respond(processAllBrandFiles(request.files))
    }
}