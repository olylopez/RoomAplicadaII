package edu.ucne.roomaplicadaii.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TipoTec")
data class TipoTecEntity(
    @PrimaryKey
    val tipoId: Int? = null,
    var descripcion: String = "",
) {
}
