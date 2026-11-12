package com.example.a040a21mikardex

import com.example.a040a21mikardex.ui.comoCalificacion
import com.example.a040a21mikardex.ui.comoPromedio
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * La escuela usa calificaciones enteras y promedios con dos decimales,
 * con punto como separador, sin importar el idioma del teléfono.
 */
class FormatoTest {

    @Test
    fun laCalificacionSeMuestraSinDecimales() {
        assertEquals("85", 85.0.comoCalificacion())
        assertEquals("100", 100.0.comoCalificacion())
        assertEquals("0", 0.0.comoCalificacion())
    }

    @Test
    fun laCalificacionVaciaSeMuestraConGuion() {
        assertEquals("—", (null as Double?).comoCalificacion())
    }

    @Test
    fun elPromedioSeMuestraConDosDecimales() {
        assertEquals("85.33", 85.3333.comoPromedio())
        assertEquals("85.00", 85.0.comoPromedio())
    }

    @Test
    fun elPromedioVacioSeMuestraConGuion() {
        assertEquals("—", (null as Double?).comoPromedio())
    }
}
