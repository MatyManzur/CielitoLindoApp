package com.example.cielitolindo.presentation.pagos.gastos_detail

import com.example.cielitolindo.domain.model.Gasto
import com.example.cielitolindo.presentation.util.LoadingInfo

data class GastosDetailState(
    val id: String = "",
    val gasto: Gasto? = null,
    val showDeleteConfirmationDialog: Boolean = false,
    val loadingInfo: LoadingInfo = LoadingInfo()
)
