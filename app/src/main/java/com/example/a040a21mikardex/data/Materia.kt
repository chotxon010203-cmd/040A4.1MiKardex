package com.example.a040a21mikardex.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Una materia del cardex. Cada propiedad es una columna de la tabla "materias".
 * Las tres calificaciones son opcionales porque se pueden dejar vacías.
 */
@Entity(tableName = "materias")
data class Materia(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val semestre: Int,
    val califNormal: Double? = null,
    val califRepeticion: Double? = null,
    val califEspecial: Double? = null,
) {
    /** La calificación que cuenta: la más alta de las cargadas, o null si no hay ninguna. */
    val definitiva: Double?
        get() = listOfNotNull(califNormal, califRepeticion, califEspecial).maxOrNull()
}
