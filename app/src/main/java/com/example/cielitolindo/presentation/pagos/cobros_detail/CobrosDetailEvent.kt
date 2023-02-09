package com.example.cielitolindo.presentation.pagos.cobros_detail

sealed class CobrosDetailEvent {
    object OnEdit : CobrosDetailEvent()
    object OnShowDeleteConfirmationDialog : CobrosDetailEvent()
    object OnHideDeleteConfirmationDialog : CobrosDetailEvent()
    object OnDelete : CobrosDetailEvent()
}
