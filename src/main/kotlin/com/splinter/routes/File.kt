import com.splinter.engine.merger.constructResponseFile
import com.splinter.engine.merger.findDuplicateKeysBetweenFiles
import com.splinter.engine.parser.decodeJsonFileFromString
import com.splinter.model.PostRequest
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getResultingFile() {
    get("/") {
        val s = "{\"name\": \"en_NL.i18n\", \"json\":{\"test_key\":\"test value\",\"test_key_object\":{\"test_key\":\"test value\"}}}"
        call.respond(decodeJsonFileFromString(s))
    }
}

fun Route.getDuplicateKeyFromFiles() {
    post("/file") {
        val data = call.receive<PostRequest>()
        val responseMap = findDuplicateKeysBetweenFiles(data.data)
        val responseJson = constructResponseFile("results.json", responseMap)

        call.respond(responseJson)
    }
}