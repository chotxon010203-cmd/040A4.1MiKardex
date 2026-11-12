package com.example.a040a21mikardex.data

/** Resultado de validar lo que el usuario escribió en el formulario. */
sealed interface Validacion {
    data class Ok(val materia: Materia) : Validacion
    data class Error(val mensaje: String) : Validacion
}

private const val SEMESTRE_MINIMO = 1
private const val SEMESTRE_MAXIMO = 12
private const val CALIFICACION_MINIMA = 0
private const val CALIFICACION_MAXIMA = 100

/**
 * Convierte los textos del formulario en una Materia, o explica qué está mal.
 * Es lógica pura: no sabe nada de Compose ni de la base de datos, por eso se testea sin emulador.
 */
fun validarMateria(
    nombre: String,
    semestre: String,
    normal: String,
    repeticion: String,
    especial: String,
): Validacion {
    val nombreLimpio = nombre.trim()
    if (nombreLimpio.isEmpty()) return Validacion.Error("Escribí el nombre de la materia")

    val semestreNumero = semestre.toIntOrNull()
    if (semestreNumero == null || semestreNumero !in SEMESTRE_MINIMO..SEMESTRE_MAXIMO) {
        return Validacion.Error("El semestre debe ser un número entre $SEMESTRE_MINIMO y $SEMESTRE_MAXIMO")
    }

    val calificaciones = listOf(
        "Normal" to normal,
        "Repetición" to repeticion,
        "Especial" to especial,
    ).map { (etiqueta, texto) ->
        if (texto.isBlank()) {
            null
        } else {
            // La escuela califica con enteros de 0 a 100: no se aceptan decimales.
            val valor = texto.trim().toIntOrNull()
                ?: return Validacion.Error("La calificación de $etiqueta debe ser un número entero")
            if (valor !in CALIFICACION_MINIMA..CALIFICACION_MAXIMA) {
                return Validacion.Error(
                    "La calificación de $etiqueta debe estar entre $CALIFICACION_MINIMA y $CALIFICACION_MAXIMA"
                )
            }
            valor.toDouble()
        }
    }

    if (calificaciones.all { it == null }) {
        return Validacion.Error("Cargá al menos una calificación")
    }

    return Validacion.Ok(
        Materia(
            nombre = nombreLimpio,
            semestre = semestreNumero,
            califNormal = calificaciones[0],
            califRepeticion = calificaciones[1],
            califEspecial = calificaciones[2],
        )
    )
}
