package com.splinter.model

import com.splinter.model.enums.Brand
import com.splinter.model.enums.Language
import kotlinx.serialization.Serializable

@Serializable
data class PostResponseAllFiles(
    var en: JsonFile? = null,
    var fr: JsonFile? = null,
    var brands: MutableMap<Brand, BrandFiles> = mutableMapOf()
) {
    operator fun set(lang: Language, value: JsonFile) {
        if (lang == Language.FRENCH) fr = value
        else if (lang == Language.ENGLISH) en = value
    }
}

@Serializable
data class BrandFiles (
    var en: JsonFile? = null,
    var fr: JsonFile? = null,
    var provinces: MutableMap<String, ProvinceFiles> = mutableMapOf()
) {
    operator fun set(lang: Language, value: JsonFile) {
        if (lang == Language.FRENCH) fr = value
        else if (lang == Language.ENGLISH) en = value
    }
}

@Serializable
data class ProvinceFiles (
    var en: JsonFile? = null,
    var fr: JsonFile? = null
){
    operator fun set(lang: Language, value: JsonFile) {
        if (lang == Language.FRENCH) fr = value
        else if (lang == Language.ENGLISH) en = value
    }
}

