package com.example.a040a21mikardex.data

import android.content.Context
import androidx.core.content.edit

/**
 * Datos del alumno en SharedPreferences: se conservan aunque se cierre la app.
 * Se guarda poco y suelto (nombre y número de control), por eso no va en la base de datos.
 */
class DatosAlumno(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences("alumno", Context.MODE_PRIVATE)

    var nombre: String
        get() = prefs.getString(CLAVE_NOMBRE, "").orEmpty()
        set(valor) = prefs.edit { putString(CLAVE_NOMBRE, valor) }

    var numeroControl: String
        get() = prefs.getString(CLAVE_CONTROL, "").orEmpty()
        set(valor) = prefs.edit { putString(CLAVE_CONTROL, valor) }

    /** true cuando ya hay nombre y número de control guardados. */
    val registrado: Boolean
        get() = nombre.isNotBlank() && numeroControl.isNotBlank()

    private companion object {
        const val CLAVE_NOMBRE = "nombre"
        const val CLAVE_CONTROL = "numero_control"
    }
}
