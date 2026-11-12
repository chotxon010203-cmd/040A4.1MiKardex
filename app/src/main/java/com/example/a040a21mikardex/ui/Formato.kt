package com.example.a040a21mikardex.ui

import java.util.Locale

/**
 * La escuela califica con enteros y promedia con dos decimales, usando punto.
 * Fijamos el [Locale] para que el separador no cambie con el idioma del teléfono.
 */
private val LOCALE_ESCUELA = Locale.US

/** Muestra una calificación entera (85), o un guion si no hay nota. */
internal fun Double?.comoCalificacion(): String =
    this?.let { String.format(LOCALE_ESCUELA, "%.0f", it) } ?: "—"

/** Muestra un promedio con dos decimales (85.33), o un guion si no hay promedio. */
internal fun Double?.comoPromedio(): String =
    this?.let { String.format(LOCALE_ESCUELA, "%.2f", it) } ?: "—"
