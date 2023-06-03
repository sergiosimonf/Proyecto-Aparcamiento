package dev.sergiosf.proyectoaparcamiento.errors

sealed class AparcadoError(val message: String) {
    class ValidationProblem(message: String) : AparcadoError(message)
    class ParametroNoIntroducido(message: String) : AparcadoError(message)
}