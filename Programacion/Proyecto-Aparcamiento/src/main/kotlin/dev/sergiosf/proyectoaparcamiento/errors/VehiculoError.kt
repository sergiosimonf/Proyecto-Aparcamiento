package dev.sergiosf.proyectoaparcamiento.errors

sealed class VehiculoError(val message: String) {
    class ParametroNoIntroducido(message: String) : VehiculoError(message)
    class ValidateProblem(message: String) : VehiculoError(message)
    class ParametroInvalido(message: String) : VehiculoError(message)
    class LoadJson(message: String) : VehiculoError(message)
    class SaveJson(message: String) : VehiculoError(message)
}