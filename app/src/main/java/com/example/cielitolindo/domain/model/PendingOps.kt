package com.example.cielitolindo.domain.model


class PendingOps (
    val operation: suspend (Element) -> Unit,
    val element: Element
)