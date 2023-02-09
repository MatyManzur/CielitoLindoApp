package com.example.cielitolindo.presentation.pagos.pagos_main

import com.example.cielitolindo.presentation.pagos.pagos_main.util.DateDefinitionCriteria
import com.example.cielitolindo.presentation.pagos.pagos_main.util.DateGroupCriteria
import java.time.LocalDate

sealed class PagosEvent {
    data class SetDateGroupCriteria(val groupCriteria: DateGroupCriteria): PagosEvent()
    data class SetDateDefinitionCriteria(val definitionCriteria: DateDefinitionCriteria): PagosEvent()
    data class SetCustomPeriod(val customPeriod: Pair<LocalDate, LocalDate>): PagosEvent()
    object ShowSettingsDialog: PagosEvent()
    object HideSettingsDialog: PagosEvent()
    object ShowCobrosDetail: PagosEvent()
    object HideCobrosDetail: PagosEvent()
    object ShowGastosDetail: PagosEvent()
    object HideGastosDetail: PagosEvent()
    object NextPeriod: PagosEvent()
    object PreviousPeriod: PagosEvent()
}