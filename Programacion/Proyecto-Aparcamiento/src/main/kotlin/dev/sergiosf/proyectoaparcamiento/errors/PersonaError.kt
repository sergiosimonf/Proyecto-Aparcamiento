package dev.sergiosf.proyectoaparcamiento.errors

sealed class PersonaError(val message: String) {
    class ParametroNoIntroducido(message: String) : PersonaError(message)
    class ValidateProblem(message: String) : PersonaError(message)
}