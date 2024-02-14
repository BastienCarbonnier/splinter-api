import com.splinter.engine.merger.*
import com.splinter.model.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

/**
 * Route to find duplicate key between files
 */
fun Route.getDuplicateKeyFromFiles() {
    post("/file") {
        val data = call.receive<PostRequest>()
        val responseMap = findCommonKeysBetweenFiles(data.files)
        val responseJson = constructResponseFile(UUID.randomUUID(),"results.json", responseMap)

        call.respond(responseJson)
    }
}

/**
 * Route to find common keys between a list of files and remove them from original files
 */
fun Route.removeAndGetCommonKeysFromFiles() {
    post("/files") {
        val request = call.receive<PostRequest>()
        call.respond(removeCommonKeysFromFiles(request.files))
    }
}

/**
 * Route to validate that a list of files contains all the key/value from a reference file
 */
fun Route.mergeFileContainAllKeys() {
    post("/files/validation") {
        val request = call.receive<PostValidationRequest>()
        call.respond(PostValidationResponse(isMergeFileContainAllKeys(request.files, request.referenceFile)))
    }
}

/**
 * Route to find every common keys by lang, brand, province and return the response file
 */
fun Route.removeAndGetCommonKeysFromAllBrandFiles() {
    post("/files/all-brand-files") {
        val request = call.receive<PostRequest>()
        call.respond(processAllBrandFiles(request.files))
    }
}