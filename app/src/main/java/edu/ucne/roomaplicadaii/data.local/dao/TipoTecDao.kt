package edu.ucne.roomaplicadaii.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.roomaplicadaii.data.local.entities.TecnicoEntity
import edu.ucne.roomaplicadaii.data.local.entities.TipoTecEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TipoTecDao{

    @Upsert()
    suspend fun save(tipoTec: TipoTecEntity)

    @Query("""
        Select *
        From TipoTec
        WHERE tipoId=:id
        LIMIT 1
        """)
    suspend fun find(id: Int): TipoTecEntity?

    @Delete
    suspend fun delete(tipoTec: TipoTecEntity)

    @Query("SELECT * FROM TipoTec")
    fun getAll(): Flow<List<TipoTecEntity>>
    @Query("DELETE FROM TipoTec WHERE tipoId = :tipoId")
    suspend fun deleteById(tipoId: Int)
}