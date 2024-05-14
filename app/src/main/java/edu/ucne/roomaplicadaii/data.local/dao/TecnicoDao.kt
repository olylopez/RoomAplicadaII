package edu.ucne.roomaplicadaii.data.local.dao

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Upsert
import edu.ucne.roomaplicadaii.data.local.entities.TecnicoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TecnicoDao{

    @Upsert()
    suspend fun save(tecnico: TecnicoEntity)

    @Query("""
        Select *
        From Tecnicos
        WHERE tecnicoId=:id
        LIMIT 1
        """)
    suspend fun find(id: Int): TecnicoEntity?

    @Delete
    suspend fun delete(tecnico: TecnicoEntity)

    @Query("SELECT * FROM Tecnicos")
    fun getAll(): Flow<List<TecnicoEntity>>
    @Query("DELETE FROM Tecnicos WHERE tecnicoId = :tecnicoId")
    suspend fun deleteById(tecnicoId: Int)
}

