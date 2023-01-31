package com.example.cielitolindo.domain.util

sealed class OrderType {
    object Ascending : OrderType()
    object Descending : OrderType()
}
