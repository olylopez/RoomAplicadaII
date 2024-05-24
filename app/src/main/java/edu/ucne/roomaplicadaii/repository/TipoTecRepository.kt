package edu.ucne.roomaplicadaii.repository

import edu.ucne.roomaplicadaii.data.local.dao.TipoTecDao
import edu.ucne.roomaplicadaii.data.local.entities.TipoTecEntity


class TipoTecRepository(private val tipoTecDao: TipoTecDao) {

    suspend fun saveTipoTec(tipoTec: TipoTecEntity) = tipoTecDao.save(tipoTec)
    suspend fun deleteTipoTec(tipoTec: TipoTecEntity) = tipoTecDao.delete(tipoTec)
    fun getTipoTec() = tipoTecDao.getAll()
    suspend fun getTipoTec(id: Int) = tipoTecDao.find(id)
}


