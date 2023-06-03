package dev.sergiosf.proyectoaparcamiento.di

import dev.sergiosf.proyectoaparcamiento.config.AppConfig
import dev.sergiosf.proyectoaparcamiento.repositories.aparcamiento.AparcameintoRepositoryImpl
import dev.sergiosf.proyectoaparcamiento.repositories.aparcamiento.AparcamientoRepository
import dev.sergiosf.proyectoaparcamiento.repositories.profesor.ProfesorRepository
import dev.sergiosf.proyectoaparcamiento.repositories.profesor.ProfesorRepositoryImpl
import dev.sergiosf.proyectoaparcamiento.repositories.vehiculo.VehiculosRepository
import dev.sergiosf.proyectoaparcamiento.repositories.vehiculo.VehiculosRepositoryImpl
import dev.sergiosf.proyectoaparcamiento.service.database.DataBaseService
import dev.sergiosf.proyectoaparcamiento.service.storage.StorageAparcameintoImpl
import dev.sergiosf.proyectoaparcamiento.service.storage.StorageAparcamiento
import dev.sergiosf.proyectoaparcamiento.viewmodels.AparcamientoViewModels
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val AppDIModule = module {

    singleOf(::AppConfig)

    singleOf(::DataBaseService)

    singleOf(::AparcameintoRepositoryImpl) {
        bind<AparcamientoRepository>()
    }

    singleOf(::VehiculosRepositoryImpl) {
        bind<VehiculosRepository>()
    }

    singleOf(::ProfesorRepositoryImpl) {
        bind<ProfesorRepository>()
    }

    singleOf(::StorageAparcameintoImpl) {
        bind<StorageAparcamiento>()
    }

}