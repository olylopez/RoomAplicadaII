package edu.ucne.roomaplicadaii.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.roomaplicadaii.data.local.dao.TecnicoDao
import edu.ucne.roomaplicadaii.data.local.dao.TipoTecDao
import edu.ucne.roomaplicadaii.data.local.entities.TecnicoEntity
import edu.ucne.roomaplicadaii.data.local.entities.TipoTecEntity

@Database(
    entities = [
        TipoTecEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TipoTecDb : RoomDatabase(){
    abstract fun tipoTecDao(): TipoTecDao
}
