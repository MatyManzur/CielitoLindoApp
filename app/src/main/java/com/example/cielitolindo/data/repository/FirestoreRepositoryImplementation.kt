package com.example.cielitolindo.data.repository

import com.example.cielitolindo.data.data_source.Firestore
import com.example.cielitolindo.domain.model.*
import com.example.cielitolindo.domain.repository.FirestoreRepository

class FirestoreRepositoryImplementation() : FirestoreRepository {
    private val firestore = Firestore()


    override suspend fun getClientes(): List<Cliente> {
        return firestore.getClientes()
    }

    override fun setCliente(
        cliente: Element,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        if (cliente is Cliente) {
            firestore.setCliente(cliente, onSuccessListener, onFailureListener)
        }
    }

    override fun deleteCliente(
        cliente: Element,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        if (cliente is Cliente) {
            firestore.deleteCliente(cliente, onSuccessListener, onFailureListener)
        }
    }

    override suspend fun getReservas(): List<Reserva> {
        return firestore.getReservas()
    }

    override fun setReserva(
        reserva: Element,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        if (reserva is Reserva) {
            firestore.setReserva(reserva, onSuccessListener, onFailureListener)
        }
    }

    override fun deleteReserva(
        reserva: Element,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        if (reserva is Reserva) {
            firestore.deleteReserva(reserva, onSuccessListener, onFailureListener)
        }
    }

    override suspend fun getCobros(): List<Cobro> {
        return firestore.getCobros()
    }

    override fun setCobro(
        cobro: Element,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        if (cobro is Cobro) {
            firestore.setCobro(cobro, onSuccessListener, onFailureListener)
        }
    }

    override fun deleteCobro(
        cobro: Element,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        if (cobro is Cobro) {
            firestore.deleteCobro(cobro, onSuccessListener, onFailureListener)
        }
    }

    override suspend fun getGastos(): List<Gasto> {
        return firestore.getGastos()
    }

    override fun setGasto(
        gasto: Element,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        if (gasto is Gasto) {
            firestore.setGasto(gasto, onSuccessListener, onFailureListener)
        }
    }

    override fun deleteGasto(
        gasto: Element,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        if (gasto is Gasto) {
            firestore.deleteGasto(gasto, onSuccessListener, onFailureListener)
        }
    }

}