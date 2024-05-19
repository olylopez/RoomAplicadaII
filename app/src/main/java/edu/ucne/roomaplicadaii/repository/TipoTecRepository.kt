package edu.ucne.roomaplicadaii.repository

import edu.ucne.roomaplicadaii.data.local.dao.TipoTecDao
import edu.ucne.roomaplicadaii.data.local.entities.TipoTecEntity


class TipoTecRepository(private val tipoTecDao: TipoTecDao) {

    suspend fun saveTipoTec(tipoTec: TipoTecEntity) = tipoTecDao.save(tipoTec)
    fun getTipoTec() = tipoTecDao.getAll()
}


