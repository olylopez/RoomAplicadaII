package edu.ucne.roomaplicadaii.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.ucne.roomaplicadaii.data.local.dao.ServicioTecDao
import edu.ucne.roomaplicadaii.data.local.dao.TecnicoDao
import edu.ucne.roomaplicadaii.data.local.dao.TipoTecDao
import edu.ucne.roomaplicadaii.data.local.entities.ServicioTecEntity
import edu.ucne.roomaplicadaii.data.local.entities.TecnicoEntity
import edu.ucne.roomaplicadaii.data.local.entities.TipoTecEntity

@Database(
    entities = [
        TecnicoEntity::class,
        TipoTecEntity::class,
        ServicioTecEntity::class


    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TecnicoDb : RoomDatabase(){

    abstract fun tecnicoDao(): TecnicoDao
    abstract fun tipoTecDao(): TipoTecDao
    abstract fun servicioTecDao(): ServicioTecDao

}