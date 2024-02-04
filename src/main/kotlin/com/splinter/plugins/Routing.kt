package com.splinter.plugins

import getDuplicateKeyFromFiles
import removeAndGetCommonKeysFromFiles
import getResultingFile
import io.ktor.server.application.*
import io.ktor.server.routing.*
import mergeFileContainAllKeys
import removeAndGetCommonKeysFromFilesAll

fun Application.configureRouting() {
    routing {
        getResultingFile()
        getDuplicateKeyFromFiles()
        removeAndGetCommonKeysFromFiles()
        mergeFileContainAllKeys()
        removeAndGetCommonKeysFromFilesAll()
    }
}
