package com.example.a040a21mikardex

import com.example.a040a21mikardex.data.Materia
import com.example.a040a21mikardex.data.Promedios
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PromediosTest {

    private fun materia(
        nombre: String,
        semestre: Int,
        normal: Double? = null,
        repeticion: Double? = null,
        especial: Double? = null,
    ) = Materia(
        nombre = nombre,
        semestre = semestre,
        califNormal = normal,
        califRepeticion = repeticion,
        califEspecial = especial,
    )

    @Test
    fun promedioGeneralDeUnaSolaMateria() {
        val materias = listOf(materia("Cálculo", 1, normal = 85.0))

        assertEquals(85.0, Promedios.general(materias)!!, 0.001)
    }

    @Test
    fun promedioGeneralPromediaLasDefinitivas() {
        val materias = listOf(
            materia("Cálculo", 1, normal = 80.0),
            materia("Álgebra", 1, normal = 60.0),
            materia("Física", 2, normal = 100.0),
        )

        assertEquals(80.0, Promedios.general(materias)!!, 0.001)
    }

    @Test
    fun promedioGeneralConDecimales() {
        val materias = listOf(
            materia("Cálculo", 1, normal = 80.0),
            materia("Álgebra", 1, normal = 90.0),
            materia("Física", 2, normal = 86.0),
        )

        assertEquals(85.3333, Promedios.general(materias)!!, 0.001)
    }

    @Test
    fun promedioGeneralIgnoraMateriasSinCalificacion() {
        val materias = listOf(
            materia("Cálculo", 1, normal = 90.0),
            materia("Álgebra", 1),
        )

        assertEquals(90.0, Promedios.general(materias)!!, 0.001)
    }

    @Test
    fun promedioGeneralSinCalificacionesEsNulo() {
        assertNull(Promedios.general(listOf(materia("Cálculo", 1))))
        assertNull(Promedios.general(emptyList()))
    }

    @Test
    fun promedioGeneralUsaLaDefinitivaMasAlta() {
        val materias = listOf(
            materia("Física", 3, normal = 50.0, repeticion = 90.0),
        )

        assertEquals(90.0, Promedios.general(materias)!!, 0.001)
    }

    @Test
    fun promedioPorSemestreAgrupaYOrdena() {
        val materias = listOf(
            materia("Física", 2, normal = 100.0),
            materia("Cálculo", 1, normal = 80.0),
            materia("Álgebra", 1, normal = 60.0),
        )

        val resultado = Promedios.porSemestre(materias)

        assertEquals(2, resultado.size)
        assertEquals(1, resultado[0].semestre)
        assertEquals(70.0, resultado[0].promedio, 0.001)
        assertEquals(2, resultado[0].cantidad)
        assertEquals(listOf("Cálculo", "Álgebra"), resultado[0].materias.map { it.nombre })
        assertEquals(2, resultado[1].semestre)
        assertEquals(100.0, resultado[1].promedio, 0.001)
        assertEquals(listOf("Física"), resultado[1].materias.map { it.nombre })
    }

    @Test
    fun promedioPorSemestreOmiteMateriasSinCalificacion() {
        val materias = listOf(
            materia("Cálculo", 1, normal = 80.0),
            materia("Álgebra", 1),
        )

        val resultado = Promedios.porSemestre(materias)

        assertEquals(1, resultado.size)
        assertEquals(1, resultado[0].cantidad)
        assertEquals(listOf("Cálculo"), resultado[0].materias.map { it.nombre })
    }
}
