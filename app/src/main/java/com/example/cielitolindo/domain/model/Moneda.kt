package com.example.cielitolindo.domain.model

enum class Moneda(val stringName: String, val unitString: String, val decimalPlaces: Int): Element{
    PESOS("Pesos", "AR$", 0),
    DOLARES("Dólares", "US$", 2);

    override fun toString(): String {
        return stringName
    }

    fun importeToString(importe: Float) : String {
        return String.format("%s %.${decimalPlaces}f", unitString, importe)
    }
}