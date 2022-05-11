package net.suwon.plus.data.repository.media

import kotlinx.coroutines.flow.Flow
import net.suwon.plus.data.entity.media.Album


interface AlbumRepository {

    suspend fun load(): Flow<HashMap<Long?, Album>>
}