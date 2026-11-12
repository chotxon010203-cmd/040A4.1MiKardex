package com.example.a040a21mikardex

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.a040a21mikardex.data.CardexDb
import com.example.a040a21mikardex.data.Materia
import com.example.a040a21mikardex.data.MateriaDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/** Verifica la tabla real: insertar, leer, borrar y la calificación definitiva. */
@RunWith(AndroidJUnit4::class)
class CardexDbTest {

    private lateinit var db: CardexDb
    private lateinit var dao: MateriaDao

    @Before
    fun crearBase() {
        val contexto = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(contexto, CardexDb::class.java).build()
        dao = db.materias()
    }

    @After
    fun cerrarBase() {
        db.close()
    }

    @Test
    fun guardaYDevuelveLaMateria() = runBlocking {
        dao.insertar(Materia(nombre = "Cálculo", semestre = 1, califNormal = 85.0))

        val materias = dao.observarTodas().first()

        assertEquals(1, materias.size)
        assertEquals("Cálculo", materias[0].nombre)
        assertEquals(1, materias[0].semestre)
        assertEquals(85.0, materias[0].definitiva!!, 0.001)
    }

    @Test
    fun lasTresCalificacionesSonOpcionales() = runBlocking {
        dao.insertar(Materia(nombre = "Álgebra", semestre = 2, califRepeticion = 70.0))

        val materia = dao.observarTodas().first().single()

        assertEquals(null, materia.califNormal)
        assertEquals(70.0, materia.definitiva!!, 0.001)
    }

    @Test
    fun laDefinitivaEsLaCalificacionMasAlta() {
        val materia = Materia(
            nombre = "Física",
            semestre = 3,
            califNormal = 50.0,
            califRepeticion = 90.0,
            califEspecial = 80.0,
        )

        assertEquals(90.0, materia.definitiva!!, 0.001)
    }

    @Test
    fun eliminaLaMateria() = runBlocking {
        val id = dao.insertar(Materia(nombre = "Química", semestre = 1, califNormal = 60.0))

        dao.eliminar(id)

        assertEquals(0, dao.observarTodas().first().size)
    }
}
