package edu.ucne.roomaplicadaii.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.roomaplicadaii.data.local.entities.ServicioTecEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ServicioTecDao {
    @Upsert()
    suspend fun save(servicioTec: ServicioTecEntity)

    @Query("""
        Select *
        From ServicioTec
        WHERE servicioId=:id
        LIMIT 1
        """)
    suspend fun find(id: Int): ServicioTecEntity?

    @Delete
    suspend fun delete(servicioTec: ServicioTecEntity)

    @Query("SELECT * FROM ServicioTec")
    fun getAll(): Flow<List<ServicioTecEntity>>
    @Query("DELETE FROM ServicioTec WHERE servicioId = :servicioId")
    suspend fun deleteById(servicioId: Int)
}