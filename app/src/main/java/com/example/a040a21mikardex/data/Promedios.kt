package com.example.a040a21mikardex.data

/** Un promedio por semestre y las materias que lo componen. */
data class PromedioSemestre(
    val semestre: Int,
    val promedio: Double,
    val materias: List<Materia>,
) {
    /** Cuántas materias entran en el promedio. */
    val cantidad: Int get() = materias.size
}

/**
 * Cálculo de promedios. Es matemática pura sobre la lista ya cargada:
 * no toca la base de datos, así se puede probar sin emulador.
 *
 * Regla: la calificación de una materia es la más alta de las tres cargadas;
 * las materias sin ninguna calificación no cuentan.
 */
object Promedios {

    fun general(materias: List<Materia>): Double? {
        val notas = materias.mapNotNull { it.definitiva }
        if (notas.isEmpty()) return null
        return notas.average()
    }

    fun porSemestre(materias: List<Materia>): List<PromedioSemestre> =
        materias
            .filter { it.definitiva != null }
            .groupBy { it.semestre }
            .toSortedMap()
            .map { (semestre, delSemestre) ->
                PromedioSemestre(
                    semestre = semestre,
                    promedio = delSemestre.mapNotNull { it.definitiva }.average(),
                    materias = delSemestre,
                )
            }
}
