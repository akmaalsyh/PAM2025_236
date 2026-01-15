package com.example.simkader_236.repositori

import com.example.simkader_236.room.Kader
import com.example.simkader_236.room.KaderDao
import kotlinx.coroutines.flow.Flow

interface RepositoriKader {
    fun getAllKaderStream(): Flow<List<Kader>>
    fun getKaderStream(id: Int): Flow<Kader?>
    suspend fun insertKader(kader: Kader)
    suspend fun updateKader(kader: Kader)
    suspend fun deleteKader(kader: Kader)
}

class OfflineRepositoriKader(
    private val kaderDao: KaderDao
) : RepositoriKader {

    override fun getAllKaderStream(): Flow<List<Kader>> =
        kaderDao.getAllKader()

    override fun getKaderStream(id: Int): Flow<Kader?> =
        kaderDao.getKader(id)

    override suspend fun insertKader(kader: Kader) =
        kaderDao.insert(kader)

    override suspend fun updateKader(kader: Kader) =
        kaderDao.update(kader)

    override suspend fun deleteKader(kader: Kader) =
        kaderDao.delete(kader)
}