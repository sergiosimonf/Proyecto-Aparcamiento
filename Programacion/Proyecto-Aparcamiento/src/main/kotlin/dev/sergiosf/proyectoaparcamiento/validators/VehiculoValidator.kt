package dev.sergiosf.proyectoaparcamiento.validators

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.sergiosf.proyectoaparcamiento.errors.VehiculoError
import dev.sergiosf.proyectoaparcamiento.models.Vehiculo

fun Vehiculo.validar(): Result<Vehiculo, VehiculoError> {

    val matriculaRegex = "^[0-9]{1,4}(?!.*(LL|CH))[BCDFGHJKLMNPRSTVWXYZ]{3}".toRegex()

    if (!matriculaRegex.matches(this.matricula)) {
        return Err(VehiculoError.ValidateProblem("La matricula esta mal formada"))
    }

    if (this.marca.isEmpty()) {
        return Err(VehiculoError.ParametroNoIntroducido("La marca no ha sido introducida"))
    }

    if (this.modelo.isEmpty()) {
        return Err(VehiculoError.ParametroNoIntroducido("El modelo no ha sido introducido"))
    }

    if (this.tipoVehiculo != Vehiculo.TipoVehiculo.COMBUSTION || this.tipoVehiculo != Vehiculo.TipoVehiculo.ELECTRICO || this.tipoVehiculo != Vehiculo.TipoVehiculo.HIBRIDO) {
        return Err(VehiculoError.ParametroInvalido("El tipo vehiculo no exite o no es valido"))
    }

    return Ok(this)
}