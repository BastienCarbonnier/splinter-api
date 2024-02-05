package com.splinter.model

import com.splinter.model.enums.BrandEnum
import com.splinter.model.enums.LanguageEnum
import com.splinter.model.enums.ProvinceEnum
import kotlinx.serialization.Serializable

@Serializable
data class PostResponseAllFiles(
    var en: JsonFile? = null,
    var fr: JsonFile? = null,
    var brands: MutableMap<BrandEnum, BrandFiles> = mutableMapOf()
) {
    operator fun set(lang: LanguageEnum, value: JsonFile) {
        if (lang == LanguageEnum.FRENCH) fr = value
        else if (lang == LanguageEnum.ENGLISH) en = value
    }
}

@Serializable
data class BrandFiles (
    var en: JsonFile? = null,
    var fr: JsonFile? = null,
    var provinces: MutableMap<ProvinceEnum, ProvinceFiles> = mutableMapOf()
) {
    operator fun set(lang: LanguageEnum, value: JsonFile) {
        if (lang == LanguageEnum.FRENCH) fr = value
        else if (lang == LanguageEnum.ENGLISH) en = value
    }

    operator fun get(lang: LanguageEnum): JsonFile? {
        return if (lang == LanguageEnum.FRENCH) {
            this.fr
        } else {
            this.en
        }
    }
}

@Serializable
data class ProvinceFiles (
    var en: JsonFile? = null,
    var fr: JsonFile? = null
){
    operator fun set(lang: LanguageEnum, value: JsonFile) {
        if (lang == LanguageEnum.FRENCH) fr = value
        else if (lang == LanguageEnum.ENGLISH) en = value
    }

    operator fun get(lang: LanguageEnum): JsonFile? {
        return if (lang == LanguageEnum.FRENCH) {
            this.fr
        } else {
            this.en
        }
    }
}

