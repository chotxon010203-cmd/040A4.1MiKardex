package com.example.a040a21mikardex

import com.example.a040a21mikardex.data.Validacion
import com.example.a040a21mikardex.data.validarMateria
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidacionMateriaTest {

    private fun validar(
        nombre: String = "Cálculo",
        semestre: String = "1",
        normal: String = "",
        repeticion: String = "",
        especial: String = "",
    ) = validarMateria(nombre, semestre, normal, repeticion, especial)

    private fun mensaje(resultado: Validacion): String {
        assertTrue("Se esperaba un error", resultado is Validacion.Error)
        return (resultado as Validacion.Error).mensaje
    }

    @Test
    fun rechazaNombreVacio() {
        mensaje(validar(nombre = "   ", normal = "8"))
    }

    @Test
    fun rechazaSemestreFueraDeRango() {
        mensaje(validar(semestre = "0", normal = "8"))
        mensaje(validar(semestre = "13", normal = "8"))
        mensaje(validar(semestre = "abc", normal = "8"))
    }

    @Test
    fun rechazaCalificacionNoNumerica() {
        mensaje(validar(normal = "ocho"))
    }

    @Test
    fun rechazaCalificacionFueraDeEscala() {
        mensaje(validar(normal = "101"))
        mensaje(validar(especial = "-1"))
    }

    @Test
    fun rechazaCalificacionConDecimales() {
        mensaje(validar(normal = "8.5"))
        mensaje(validar(normal = "85.5"))
        mensaje(validar(normal = "85,5"))
    }

    @Test
    fun rechazaMateriaSinNingunaCalificacion() {
        mensaje(validar(normal = "", repeticion = "", especial = ""))
    }

    @Test
    fun aceptaCalificacionEntera() {
        val resultado = validar(normal = "85")

        assertTrue(resultado is Validacion.Ok)
        assertEquals(85.0, (resultado as Validacion.Ok).materia.califNormal!!, 0.001)
    }

    @Test
    fun aceptaLosExtremosDeLaEscala() {
        assertTrue(validar(normal = "0") is Validacion.Ok)
        assertTrue(validar(especial = "100") is Validacion.Ok)
    }

    @Test
    fun armaLaMateriaConLosDatosCargados() {
        val resultado = validar(
            nombre = "  Física  ",
            semestre = "3",
            normal = "50",
            repeticion = "90",
            especial = "",
        )

        assertTrue(resultado is Validacion.Ok)
        val materia = (resultado as Validacion.Ok).materia
        assertEquals("Física", materia.nombre)
        assertEquals(3, materia.semestre)
        assertEquals(50.0, materia.califNormal!!, 0.001)
        assertEquals(90.0, materia.califRepeticion!!, 0.001)
        assertNull(materia.califEspecial)
    }
}
