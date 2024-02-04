package com.splinter.model.enums

enum class Province(val label: String) {
    NEW_BRUNSWICK("nb"),
    NEW_SCOTLAND("ns"),
    NEW_FOUNDLAND("nl"),
    PRINCE_EDWARDS("pe"),
    QUEBEC("qc"),
    ALBERTA("ab"),
    ONTARIO("on"),
    BRITISH_COLUMBIA("bc");
    companion object {
        infix fun from(value: String): Province? = entries.firstOrNull { it.label == value }
    }
}