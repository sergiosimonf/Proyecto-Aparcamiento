package dev.sergiosf.proyectoaparcamiento

import dev.sergiosf.proyectoaparcamiento.di.AppDIModule
import dev.sergiosf.proyectoaparcamiento.routes.RoutesManager
import javafx.application.Application
import javafx.stage.Stage
import org.koin.core.context.startKoin

class AparcamientoApplication : Application() {

    override fun start(stage: Stage) {
        startKoin {
            printLogger()
            modules(AppDIModule)
        }
        RoutesManager.apply {
            app = this@AparcamientoApplication
        }
        RoutesManager.initMainStage(stage)

    }
}

fun main() {
    Application.launch(AparcamientoApplication::class.java)
}