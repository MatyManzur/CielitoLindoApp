package com.example.cielitolindo.presentation.util

data class LoadingInfo(val loadingState: LoadingState = LoadingState.READY, val message: String = "") {
    companion object val loadingTimeout: Long = 2000
}
