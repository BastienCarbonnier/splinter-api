import com.splinter.engine.parser.decodeJsonFileFromString
import com.splinter.model.JsonFile
import io.ktor.http.*
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
        val jsonFile = call.receive<List<JsonFile>>()
        println(jsonFile)
        call.respondText("Json file received", status = HttpStatusCode.Created)
    }
}