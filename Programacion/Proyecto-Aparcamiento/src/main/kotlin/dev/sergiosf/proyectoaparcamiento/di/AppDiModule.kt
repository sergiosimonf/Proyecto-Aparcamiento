package dev.sergiosf.proyectoaparcamiento.di

import dev.sergiosf.proyectoaparcamiento.config.AppConfig
import org.koin.dsl.module

val AppDIModule = module {

    single { AppConfig() }


}