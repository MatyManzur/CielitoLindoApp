package com.example.cielitolindo.domain.model

enum class Moneda(val stringName: String, val unitString: String) {
    PESOS("Pesos", "AR$"),
    DOLARES("Dólares", "US$");

    override fun toString(): String {
        return stringName
    }

    fun importeToString(importe: Float) : String {
        return String.format("%s %.2f", unitString, importe)
    }
}