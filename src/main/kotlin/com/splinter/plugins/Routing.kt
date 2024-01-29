package com.splinter.plugins

import getDuplicateKeyFromFiles
import removeAndGetCommonKeysFromFiles
import getResultingFile
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        getResultingFile()
        getDuplicateKeyFromFiles()
        removeAndGetCommonKeysFromFiles()
    }
}
