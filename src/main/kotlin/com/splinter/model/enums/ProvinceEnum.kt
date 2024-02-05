package com.splinter.model.enums

enum class ProvinceEnum(val label: String) {
    NEW_BRUNSWICK("nb"),
    NEW_SCOTLAND("ns"),
    NEW_FOUNDLAND("nl"),
    PRINCE_EDWARDS("pe"),
    QUEBEC("qc"),
    ALBERTA("ab"),
    ONTARIO("on"),
    BRITISH_COLUMBIA("bc");
    companion object {
        private infix fun from(value: String): ProvinceEnum? = entries.firstOrNull { it.label == value }
        fun getProvinceFromFileName(name: String, brand: BrandEnum, lang: LanguageEnum): ProvinceEnum? {
            return from(name.replace(brand.label, "")
                .replace("${lang.label}_CA", "")
                .replace("_", "")
                .replace(".json", ""))
        }
    }
}