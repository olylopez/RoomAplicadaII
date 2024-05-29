package edu.ucne.roomaplicadaii.repository

import edu.ucne.roomaplicadaii.data.local.dao.ServicioTecDao
import edu.ucne.roomaplicadaii.data.local.entities.ServicioTecEntity

class ServicioTecRepository(private val servicioTecDao: ServicioTecDao) {
    suspend fun saveServicioTec(servicioTec: ServicioTecEntity) = servicioTecDao.save(servicioTec)
    //suspend fun deleteTipoTec(tipoTec: TipoTecEntity) = tipoTecDao.delete(tipoTec)
    fun getServicioTec() = servicioTecDao.getAll()
    suspend fun getServicioTec(id: Int) = servicioTecDao.find(id)
}