package com.example.cielitolindo.data.data_source

import androidx.compose.runtime.rememberCoroutineScope
import androidx.room.PrimaryKey
import com.example.cielitolindo.domain.model.Cliente
import com.example.cielitolindo.domain.model.Cobro
import com.example.cielitolindo.domain.model.Gasto
import com.example.cielitolindo.domain.model.Reserva
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Firestore {
    private val db = Firebase.firestore

    suspend fun getClientes(): List<Cliente> {
        val clientes = mutableListOf<Cliente>()
        db.collection("cliente").get().await().forEach { document ->
            clientes.add(document.toObject<FirestoreCliente>().toCliente())
        }
        return clientes
    }

    fun setCliente(cliente: Cliente, onSuccessListener: () -> Unit, onFailureListener: (Exception) -> Unit) {
        db.collection("cliente").document(cliente.id).set(FirestoreCliente(cliente))
            .addOnSuccessListener { onSuccessListener() }
            .addOnFailureListener(onFailureListener)
    }

    fun deleteCliente(cliente: Cliente, onSuccessListener: () -> Unit, onFailureListener: (Exception) -> Unit) {

        db.collection("cliente").document(cliente.id).delete()
            .addOnSuccessListener { onSuccessListener() }
            .addOnFailureListener(onFailureListener)
    }

    suspend fun getReservas(): List<Reserva> {
        val reservas = mutableListOf<Reserva>()
        db.collection("reserva").get().await().forEach { document ->
            reservas.add(document.toObject())
        }
        return reservas
    }

    fun setReserva(reserva: Reserva, onSuccessListener: () -> Unit, onFailureListener: (Exception) -> Unit) {
        db.collection("reserva").document(reserva.id).set(reserva)
            .addOnSuccessListener { onSuccessListener() }
            .addOnFailureListener(onFailureListener)
    }

    fun deleteReserva(reserva: Reserva, onSuccessListener: () -> Unit, onFailureListener: (Exception) -> Unit) {
        db.collection("reserva").document(reserva.id).delete()
            .addOnSuccessListener { onSuccessListener() }
            .addOnFailureListener(onFailureListener)
    }

    suspend fun getGastos(): List<Gasto> {
        val gastos = mutableListOf<Gasto>()
        db.collection("gasto").get().await().forEach { document ->
            gastos.add(document.toObject())
        }
        return gastos
    }

    fun setGasto(gasto: Gasto, onSuccessListener: () -> Unit, onFailureListener: (Exception) -> Unit) {
        db.collection("gasto").document(gasto.id).set(gasto)
            .addOnSuccessListener { onSuccessListener() }
            .addOnFailureListener(onFailureListener)
    }

    fun deleteGasto(gasto: Gasto, onSuccessListener: () -> Unit, onFailureListener: (Exception) -> Unit) {
        db.collection("gasto").document(gasto.id).delete()
            .addOnSuccessListener { onSuccessListener() }
            .addOnFailureListener(onFailureListener)
    }

    suspend fun getCobros(): List<Cobro> {
        val cobros = mutableListOf<Cobro>()
        db.collection("cobro").get().await().forEach { document ->
            cobros.add(document.toObject())
        }
        return cobros
    }

    fun setCobro(cobro: Cobro, onSuccessListener: () -> Unit, onFailureListener: (Exception) -> Unit) {
        db.collection("cobro").document(cobro.id).set(cobro)
            .addOnSuccessListener { onSuccessListener() }
            .addOnFailureListener(onFailureListener)
    }

    fun deleteCobro(cobro: Cobro, onSuccessListener: () -> Unit, onFailureListener: (Exception) -> Unit) {
        db.collection("cobro").document(cobro.id).delete()
            .addOnSuccessListener { onSuccessListener() }
            .addOnFailureListener(onFailureListener)
    }
}

val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

data class FirestoreCliente(
    val id: String,
    val nombre: String,
    val apellido: String?,
    val fechaInscripcion: String,
    val dni: Int?,
    val direccion: String?,
    val localidad: String?,
    val provincia: String?,
    val telefono: String?,
    val email: String?,
    val observaciones: String?
) {

    fun toCliente(): Cliente {
        return Cliente(
            id,
            nombre,
            apellido,
            LocalDate.parse(fechaInscripcion, formatter),
            dni,
            direccion,
            localidad,
            provincia,
            telefono,
            email,
            observaciones
        )
    }
    constructor(cliente: Cliente) : this(
        cliente.id,
        cliente.nombre,
        cliente.apellido,
        cliente.fechaInscripcion.format(formatter),
        cliente.dni,
        cliente.direccion,
        cliente.localidad,
        cliente.provincia,
        cliente.telefono,
        cliente.email,
        cliente.observaciones
    )

    constructor() : this("", "", null, "", null, null, null, null, null, null, null)
}