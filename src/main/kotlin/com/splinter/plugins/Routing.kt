package com.splinter.plugins

import getDuplicateKeyFromFiles
import removeAndGetCommonKeysFromFiles
import io.ktor.server.application.*
import io.ktor.server.routing.*
import mergeFileContainAllKeys
import removeAndGetCommonKeysFromAllBrandFiles

fun Application.configureRouting() {
    routing {
        getDuplicateKeyFromFiles()
        removeAndGetCommonKeysFromFiles()
        mergeFileContainAllKeys()
        removeAndGetCommonKeysFromAllBrandFiles()
    }
}
