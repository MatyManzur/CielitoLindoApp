package com.example.cielitolindo.data.data_source

import androidx.compose.runtime.rememberCoroutineScope
import androidx.room.PrimaryKey
import com.example.cielitolindo.domain.model.*
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

    fun setCliente(
        cliente: Cliente,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        db.collection("cliente").document(cliente.id).set(FirestoreCliente(cliente))
            .addOnSuccessListener { onSuccessListener() }
            .addOnFailureListener(onFailureListener)
    }

    fun deleteCliente(
        cliente: Cliente,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        db.collection("cliente").document(cliente.id).delete()
            .addOnSuccessListener { onSuccessListener() }
            .addOnFailureListener(onFailureListener)
    }

    suspend fun getReservas(): List<Reserva> {
        val reservas = mutableListOf<Reserva>()
        db.collection("reserva").get().await().forEach { document ->
            reservas.add(document.toObject<FirestoreReserva>().toReserva())
        }
        return reservas
    }

    fun setReserva(
        reserva: Reserva,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        db.collection("reserva").document(reserva.id).set(FirestoreReserva(reserva))
            .addOnSuccessListener { onSuccessListener() }
            .addOnFailureListener(onFailureListener)
    }

    fun deleteReserva(
        reserva: Reserva,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        db.collection("reserva").document(reserva.id).delete()
            .addOnSuccessListener { onSuccessListener() }
            .addOnFailureListener(onFailureListener)
    }

    suspend fun getGastos(): List<Gasto> {
        val gastos = mutableListOf<Gasto>()
        db.collection("gasto").get().await().forEach { document ->
            gastos.add(document.toObject<FirestoreGasto>().toGasto())
        }
        return gastos
    }

    fun setGasto(
        gasto: Gasto,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        db.collection("gasto").document(gasto.id).set(FirestoreGasto(gasto))
            .addOnSuccessListener { onSuccessListener() }
            .addOnFailureListener(onFailureListener)
    }

    fun deleteGasto(
        gasto: Gasto,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        db.collection("gasto").document(gasto.id).delete()
            .addOnSuccessListener { onSuccessListener() }
            .addOnFailureListener(onFailureListener)
    }

    suspend fun getCobros(): List<Cobro> {
        val cobros = mutableListOf<Cobro>()
        db.collection("cobro").get().await().forEach { document ->
            cobros.add(document.toObject<FirestoreCobro>().toCobro())
        }
        return cobros
    }

    fun setCobro(
        cobro: Cobro,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        db.collection("cobro").document(cobro.id).set(FirestoreCobro(cobro))
            .addOnSuccessListener { onSuccessListener() }
            .addOnFailureListener(onFailureListener)
    }

    fun deleteCobro(
        cobro: Cobro,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
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

data class FirestoreReserva(
    val id: String,
    val clienteId: String,
    val casa: String,
    val fechaIngreso: String,
    val fechaEgreso: String,
    val importeTotal: Float,
    val moneda: String,
    val observaciones: String?,
) {
    fun toReserva(): Reserva {
        return Reserva(
            id = id,
            clienteId = clienteId,
            casa = Casa.valueOf(casa),
            fechaIngreso = LocalDate.parse(fechaIngreso, formatter),
            fechaEgreso = LocalDate.parse(fechaEgreso, formatter),
            importeTotal = importeTotal,
            moneda = Moneda.valueOf(moneda),
            observaciones = observaciones
        )
    }

    constructor(reserva: Reserva) : this(
        id = reserva.id,
        clienteId = reserva.clienteId,
        casa = reserva.casa.name,
        fechaIngreso = reserva.fechaIngreso.format(formatter),
        fechaEgreso = reserva.fechaEgreso.format(formatter),
        importeTotal = reserva.importeTotal,
        moneda = reserva.moneda.name,
        observaciones = reserva.observaciones
    )

    constructor() : this("", "", "", "", "", 0f, "", null)
}

data class FirestoreCobro(
    val id: String,
    val clienteId: String,
    val reservaId: String,
    val fechaPago: String,
    val modoPago: String?,
    val descripcion: String?,
    val importe: Float,
    val moneda: String
) {
    fun toCobro(): Cobro {
        return Cobro(
            id = id,
            clienteId = clienteId,
            reservaId = reservaId,
            fechaPago = LocalDate.parse(fechaPago, formatter),
            modoPago = modoPago,
            descripcion = descripcion,
            importe = importe,
            moneda = Moneda.valueOf(moneda)
        )
    }

    constructor(cobro: Cobro) : this(
        id = cobro.id,
        clienteId = cobro.clienteId,
        reservaId = cobro.reservaId,
        fechaPago = cobro.fechaPago.format(formatter),
        modoPago = cobro.modoPago,
        descripcion = cobro.descripcion,
        importe = cobro.importe,
        moneda = cobro.moneda.name
    )

    constructor() : this("", "", "", "", null, null, 0f, "")
}

data class FirestoreGasto(
    val id: String,
    val fecha: String,
    val descripcion: String?,
    val categoria: String,
    val importe: Float,
    val moneda: String
) {
    fun toGasto(): Gasto {
        return Gasto(
            id = id,
            fecha = LocalDate.parse(fecha, formatter),
            descripcion = descripcion,
            categoria = Categoria.valueOf(categoria),
            importe = importe,
            moneda = Moneda.valueOf(moneda)
        )
    }

    constructor(gasto: Gasto) : this(
        id = gasto.id,
        fecha = gasto.fecha.format(formatter),
        descripcion = gasto.descripcion,
        categoria = gasto.categoria.name,
        importe = gasto.importe,
        moneda = gasto.moneda.name
    )

    constructor() : this("", "", null, "", 0f, "")
}