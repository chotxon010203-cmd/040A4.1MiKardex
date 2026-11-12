package com.example.a040a21mikardex.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Las operaciones sobre la tabla. Room genera la implementación a partir de estas anotaciones.
 */
@Dao
interface MateriaDao {

    @Insert
    suspend fun insertar(materia: Materia): Long

    /** Flow: cada vez que la tabla cambie, la UI que lo observa se actualiza sola. */
    @Query("SELECT * FROM materias ORDER BY semestre ASC, nombre ASC")
    fun observarTodas(): Flow<List<Materia>>

    @Query("DELETE FROM materias WHERE id = :id")
    suspend fun eliminar(id: Long)
}
