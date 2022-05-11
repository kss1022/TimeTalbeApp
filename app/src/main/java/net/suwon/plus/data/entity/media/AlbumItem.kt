package net.suwon.plus.data.entity.media

import android.content.ContentUris
import android.net.Uri

class AlbumItem constructor(
    val album: Album,
)  {
    fun getRecentMediaUri(): Uri {
        return ContentUris.withAppendedId(album.contentUri, album.recentMediaId)
    }
}