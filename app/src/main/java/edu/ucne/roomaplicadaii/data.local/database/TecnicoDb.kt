package edu.ucne.roomaplicadaii.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.roomaplicadaii.data.local.dao.TecnicoDao
import edu.ucne.roomaplicadaii.data.local.dao.TipoTecDao
import edu.ucne.roomaplicadaii.data.local.entities.TecnicoEntity

@Database(
    entities = [
        TecnicoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TecnicoDb : RoomDatabase(){
    abstract fun tecnicoDao(): TecnicoDao
    abstract fun tipoTecDao(): TipoTecDao
}