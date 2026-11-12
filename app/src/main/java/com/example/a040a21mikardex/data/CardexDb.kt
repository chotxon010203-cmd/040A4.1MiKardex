package com.example.a040a21mikardex.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * El punto de entrada a la base de datos. Una sola tabla, como pide el enunciado.
 * exportSchema = false: no exportamos el esquema porque la migración no cambia columnas.
 */
@Database(entities = [Materia::class], version = 2, exportSchema = false)
abstract class CardexDb : RoomDatabase() {

    abstract fun materias(): MateriaDao

    companion object {
        /**
         * Las calificaciones pasaron de la escala 0–10 con un decimal a la escala 0–100 entera.
         * Las columnas no cambian de tipo (siguen siendo REAL), así que la migración solo
         * convierte los datos ya guardados: 8.5 → 85, 10.0 → 100.
         */
        internal val MIGRACION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                COLUMNAS_CALIFICACION.forEach { columna ->
                    db.execSQL(
                        "UPDATE materias SET $columna = CAST(ROUND($columna * 10) AS INTEGER) " +
                            "WHERE $columna IS NOT NULL"
                    )
                }
            }
        }

        private val COLUMNAS_CALIFICACION =
            listOf("califNormal", "califRepeticion", "califEspecial")

        @Volatile
        private var instancia: CardexDb? = null

        /** Una sola instancia para toda la app: abrir la base dos veces es caro y peligroso. */
        fun obtener(context: Context): CardexDb =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    CardexDb::class.java,
                    "cardex.db"
                )
                    .addMigrations(MIGRACION_1_2)
                    .build()
                    .also { instancia = it }
            }
    }
}
