package com.example.cielitolindo.di

import android.app.Application
import androidx.room.Room
import com.example.cielitolindo.data.data_source.ClienteDatabase
import com.example.cielitolindo.data.data_source.CobroDatabase
import com.example.cielitolindo.data.data_source.GastoDatabase
import com.example.cielitolindo.data.data_source.ReservaDatabase
import com.example.cielitolindo.data.repository.*
import com.example.cielitolindo.domain.repository.*
import com.example.cielitolindo.domain.use_case.clientes.*
import com.example.cielitolindo.domain.use_case.cobros.*
import com.example.cielitolindo.domain.use_case.gastos.*
import com.example.cielitolindo.domain.use_case.reservas.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CielitoLindoModule {
    @Provides
    @Singleton
    fun provideClienteDatabase(app: Application): ClienteDatabase {
        return Room.databaseBuilder(
            app,
            ClienteDatabase::class.java,
            "cliente_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideClienteRepository(db: ClienteDatabase): ClienteRepository {
        return ClienteRepositoryImplementation(db.clienteDao)
    }

    @Provides
    @Singleton
    fun provideReservaDatabase(app: Application): ReservaDatabase {
        return Room.databaseBuilder(
            app,
            ReservaDatabase::class.java,
            "reserva_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideReservaRepository(db: ReservaDatabase): ReservaRepository {
        return ReservaRepositoryImplementation(db.reservaDao)
    }

    @Provides
    @Singleton
    fun provideCobroDatabase(app: Application): CobroDatabase {
        return Room.databaseBuilder(
            app,
            CobroDatabase::class.java,
            "cobro_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideCobroRepository(db: CobroDatabase): CobroRepository {
        return CobroRepositoryImplementation(db.cobroDao)
    }

    @Provides
    @Singleton
    fun provideGastoDatabase(app: Application): GastoDatabase {
        return Room.databaseBuilder(
            app,
            GastoDatabase::class.java,
            "gasto_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideGastoRepository(db: GastoDatabase): GastoRepository {
        return GastoRepositoryImplementation(db.gastoDao)
    }

    @Provides
    @Singleton
    fun provideFirestoreRepository(): FirestoreRepository {
        return FirestoreRepositoryImplementation()
    }

    @Provides
    @Singleton
    fun provideClienteUseCases(
        clienteRepository: ClienteRepository,
        reservaRepository: ReservaRepository,
        firestoreRepository: FirestoreRepository
    ): ClienteUseCases {
        return ClienteUseCases(
            getClientes = GetClientes(clienteRepository),
            deleteCliente = DeleteCliente(
                clienteRepository,
                reservaRepository,
                firestoreRepository
            ),
            addCliente = AddCliente(clienteRepository, firestoreRepository),
            fetchClientes = FetchClientes(clienteRepository, firestoreRepository),
            getCliente = GetCliente(clienteRepository),
        )
    }

    @Provides
    @Singleton
    fun provideReservaUseCases(
        clienteRepository: ClienteRepository,
        reservaRepository: ReservaRepository,
        cobroRepository: CobroRepository,
        firestoreRepository: FirestoreRepository
    ): ReservaUseCases {
        return ReservaUseCases(
            addReserva = AddReserva(reservaRepository, clienteRepository, firestoreRepository),
            countReservasOfCasaInRange = CountReservasOfCasaInRange(reservaRepository),
            deleteReserva = DeleteReserva(reservaRepository, cobroRepository, firestoreRepository),
            fetchReservas = FetchReservas(reservaRepository, firestoreRepository),
            getAllReservas = GetAllReservas(reservaRepository),
            getReserva = GetReserva(reservaRepository),
            getReservasFromCliente = GetReservasFromCliente(reservaRepository),
            getReservasInRange = GetReservasInRange(reservaRepository),
        )
    }

    @Provides
    @Singleton
    fun provideCobroUseCases(
        cobroRepository: CobroRepository,
        reservaRepository: ReservaRepository,
        firestoreRepository: FirestoreRepository
    ): CobroUseCases {
        return CobroUseCases(
            addCobro = AddCobro(cobroRepository, reservaRepository, firestoreRepository),
            deleteCobro = DeleteCobro(cobroRepository, firestoreRepository),
            fetchCobros = FetchCobros(cobroRepository, firestoreRepository),
            getCobro = GetCobro(cobroRepository),
            getCobrosFromReserva = GetCobrosFromReserva(cobroRepository),
            getCobrosByPaymentDate = GetCobrosByPaymentDate(cobroRepository),
            getCobrosByReservaDate = GetCobrosByReservaDate(cobroRepository, reservaRepository),
            getCobrosFromCliente = GetCobrosFromCliente(cobroRepository)
        )
    }

    @Provides
    @Singleton
    fun provideGastoUseCases(
        gastoRepository: GastoRepository,
        firestoreRepository: FirestoreRepository
    ): GastoUseCases {
        return GastoUseCases(
            addGasto = AddGasto(gastoRepository, firestoreRepository),
            deleteGasto = DeleteGasto(gastoRepository, firestoreRepository),
            fetchGastos = FetchGastos(gastoRepository, firestoreRepository),
            getGasto = GetGasto(gastoRepository),
            getGastosInRange = GetGastosInRange(gastoRepository),
        )
    }
}