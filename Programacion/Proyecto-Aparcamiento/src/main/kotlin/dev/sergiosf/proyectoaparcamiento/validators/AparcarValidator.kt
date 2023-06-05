package dev.sergiosf.proyectoaparcamiento.validators

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.sergiosf.proyectoaparcamiento.errors.AparcadoError
import dev.sergiosf.proyectoaparcamiento.models.Aparcamiento


fun Aparcamiento.validar(): Result<Aparcamiento, AparcadoError> {
    val matriculaRegex = "^[0-9]{1,4}(?!.*(LL|CH))[BCDFGHJKLMNPRSTVWXYZ]{3}".toRegex()

    if (this.propietario.isEmpty()) {
        return Err(AparcadoError.ParametroNoIntroducido("El nombre del propietario no puede estar vacio"))
    }

    if (!matriculaRegex.matches(this.matricula)) {
        return Err(AparcadoError.ValidationProblem("La matricula esta mal formada"))
    }

    return Ok(this)
}