package edu.ucne.roomaplicadaii.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "ServicioTec",
    foreignKeys = [
        ForeignKey(
            entity = TecnicoEntity::class,
            parentColumns = ["tecnicoId"],
            childColumns = ["tecnicoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["tecnicoId"])]
)
data class ServicioTecEntity(
    @PrimaryKey(autoGenerate = true) val servicioId: Int? = null,
    val fecha: Date,
    val tecnicoId: Int? = null,
    val cliente: String? = "",
    val descripcion: String? = "",
    val total: Double? = null
)

