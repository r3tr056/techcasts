package com.xdivision.techcasts.datasource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseDataSource @Inject constructor(
    private val dataSource: AudioDataSource,
    private val audioMapper: AudioMapper,
    private val audioDao: AudioDao
) : AudioPlayerDataSource() {
    override suspend fun getAudio() = withContext(Dispatchers.IO) {
        state = State.STATE_INITIALIZING
        val audioResource = dataSource.getAllAudio()
        val audioDTOList = if (audioResource is Resource.Success) {
            audioResource.data ?: emptyList()
        } else emptyList()

        allAudio = audioMapper.toDomainList(audioDTOList)
        audioDao.insertSong(allAudio)
        state = State.STATE_INITIALIZED
    }
}