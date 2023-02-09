package com.example.cielitolindo.presentation.pagos.gastos_detail

sealed class GastosDetailEvent {
    object OnEdit : GastosDetailEvent()
    object OnShowDeleteConfirmationDialog : GastosDetailEvent()
    object OnHideDeleteConfirmationDialog : GastosDetailEvent()
    object OnDelete : GastosDetailEvent()
}
