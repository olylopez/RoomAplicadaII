package edu.ucne.roomaplicadaii.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Tecnicos")
data class TecnicoEntity(
    @PrimaryKey
    val tecnicoId: Int? = null,
    var nombres: String = "",
    var sueldoHora: Double = 0.0
)