package com.example.cielitolindo.domain.model

enum class Casa(val stringName: String) {
    CELESTE("Celeste"),
    NARANJA("Naranja"),
    VERDE("Verde");

    override fun toString(): String {
        return stringName
    }
}