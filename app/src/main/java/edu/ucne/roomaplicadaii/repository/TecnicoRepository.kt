package edu.ucne.roomaplicadaii.repository

import edu.ucne.roomaplicadaii.data.local.dao.TecnicoDao
import edu.ucne.roomaplicadaii.data.local.entities.TecnicoEntity

class TecnicoRepository(private val tecnicoDao: TecnicoDao) {

    suspend fun saveTecnico(tecnico: TecnicoEntity) = tecnicoDao.save(tecnico)
    suspend fun deleteTecnico(tecnico: TecnicoEntity) = tecnicoDao.delete(tecnico)

    fun getTenicos() = tecnicoDao.getAll()

    suspend fun getTecnico(id: Int) = tecnicoDao.find(id)
}