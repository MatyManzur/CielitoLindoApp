package com.example.cielitolindo.presentation.util

fun colorVariation(original: Float, hash: Int, seed: Int, maxVariation: Float = .1f) : Float {
    val ans = ((((seed * hash) % 100) / 100f) * 2 * maxVariation) - maxVariation
    if (original.plus(ans) > 1f) return 1f
    if (original.plus(ans) < 0f) return 0f
    return original.plus(ans)
}