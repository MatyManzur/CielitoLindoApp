package com.example.cielitolindo.domain.repository

import com.example.cielitolindo.domain.model.*

interface FirestoreRepository {

    //cliente
    suspend fun getClientes(): List<Cliente>
    fun setCliente(cliente: Element, onSuccessListener: () -> Unit, onFailureListener: (Exception) -> Unit)
    fun deleteCliente(cliente: Element, onSuccessListener: () -> Unit, onFailureListener: (Exception) -> Unit)
    //reserva
    suspend fun getReservas(): List<Reserva>
    fun setReserva(reserva: Element, onSuccessListener: () -> Unit, onFailureListener: (Exception) -> Unit)
    fun deleteReserva(reserva: Element, onSuccessListener: () -> Unit, onFailureListener: (Exception) -> Unit)
    //cobro
    suspend fun getCobros(): List<Cobro>
    fun setCobro(cobro: Element, onSuccessListener: () -> Unit, onFailureListener: (Exception) -> Unit)
    fun deleteCobro(cobro: Element, onSuccessListener: () -> Unit, onFailureListener: (Exception) -> Unit)
    //gasto
    suspend fun getGastos(): List<Gasto>
    fun setGasto(gasto: Element, onSuccessListener: () -> Unit, onFailureListener: (Exception) -> Unit)
    fun deleteGasto(gasto: Element, onSuccessListener: () -> Unit, onFailureListener: (Exception) -> Unit)
}