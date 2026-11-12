package com.example.a040a21mikardex

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.a040a21mikardex.data.CardexDb
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * La migración 1 → 2 convierte las calificaciones ya guardadas de la escala 0–10
 * a la escala 0–100 entera. Se prueba sobre una base con el esquema real de la versión 1.
 */
@RunWith(AndroidJUnit4::class)
class MigracionEscalaTest {

    @Test
    fun multiplicaPorDiezLasCalificacionesExistentes() {
        val contexto = InstrumentationRegistry.getInstrumentation().targetContext
        val archivo = File(contexto.cacheDir, "migracion-${System.nanoTime()}.db")
        val helper = FrameworkSQLiteOpenHelperFactory().create(
            SupportSQLiteOpenHelper.Configuration.builder(contexto)
                .name(archivo.absolutePath)
                .callback(object : SupportSQLiteOpenHelper.Callback(1) {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        db.execSQL(
                            "CREATE TABLE IF NOT EXISTS `materias` (" +
                                "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                                "`nombre` TEXT NOT NULL, `semestre` INTEGER NOT NULL, " +
                                "`califNormal` REAL, `califRepeticion` REAL, `califEspecial` REAL)"
                        )
                        db.execSQL(
                            "INSERT INTO materias (nombre, semestre, califNormal, califRepeticion, califEspecial) " +
                                "VALUES ('Cálculo', 1, 8.5, NULL, NULL)"
                        )
                        db.execSQL(
                            "INSERT INTO materias (nombre, semestre, califNormal, califRepeticion, califEspecial) " +
                                "VALUES ('Física', 2, 5.0, 9.0, NULL)"
                        )
                        db.execSQL(
                            "INSERT INTO materias (nombre, semestre, califNormal, califRepeticion, califEspecial) " +
                                "VALUES ('Química', 1, NULL, NULL, 10.0)"
                        )
                    }

                    override fun onUpgrade(
                        db: SupportSQLiteDatabase,
                        oldVersion: Int,
                        newVersion: Int,
                    ) = Unit
                })
                .build()
        )

        val db = helper.writableDatabase
        try {
            CardexDb.MIGRACION_1_2.migrate(db)

            db.query(
                "SELECT nombre, califNormal, califRepeticion, califEspecial FROM materias ORDER BY id"
            ).use { cursor ->
                assertTrue(cursor.moveToNext())
                assertEquals("Cálculo", cursor.getString(0))
                assertEquals(85.0, cursor.getDouble(1), 0.001)
                assertTrue(cursor.isNull(2))
                assertTrue(cursor.isNull(3))

                assertTrue(cursor.moveToNext())
                assertEquals("Física", cursor.getString(0))
                assertEquals(50.0, cursor.getDouble(1), 0.001)
                assertEquals(90.0, cursor.getDouble(2), 0.001)
                assertTrue(cursor.isNull(3))

                assertTrue(cursor.moveToNext())
                assertEquals("Química", cursor.getString(0))
                assertTrue(cursor.isNull(1))
                assertTrue(cursor.isNull(2))
                assertEquals(100.0, cursor.getDouble(3), 0.001)
            }
        } finally {
            helper.close()
            archivo.delete()
        }
    }
}
