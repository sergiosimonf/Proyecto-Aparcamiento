package dev.sergiosf.proyectoaparcamiento.validators

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.sergiosf.proyectoaparcamiento.errors.PersonaError
import dev.sergiosf.proyectoaparcamiento.models.Profesor

fun Profesor.validar(): Result<Profesor, PersonaError> {

    val dniRegex = "^[0-9]{8,8}[A-Za-z]\$\n".toRegex()
    val stringRegex = "[a-zA-Z]+\\d*".toRegex()

    if (this.nombre.isEmpty()) {
        return Err(PersonaError.ParametroNoIntroducido("El nombre no puede estar vacio"))
    }

    if (this.apellido.isEmpty()) {
        return Err(PersonaError.ParametroNoIntroducido("El apellido no puede estar vacio"))
    }

    if (!dniRegex.matches(this.dni)) {
        return Err(PersonaError.ValidateProblem("El dni esta mal formado"))
    }

    if (stringRegex.matches(this.nombre)) {
        return Err(PersonaError.ValidateProblem("El nombre esta mal formado"))
    }

    if (!stringRegex.matches(this.apellido)) {
        return Err(PersonaError.ValidateProblem("El apellido esta mal formado"))
    }

    return Ok(this)
}