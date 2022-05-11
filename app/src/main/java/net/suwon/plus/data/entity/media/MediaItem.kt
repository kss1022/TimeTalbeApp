package net.suwon.plus.data.entity.media

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView

class MediaItem constructor(
    val media: Media,
)  {

    fun getUri(): Uri {
        return ContentUris.withAppendedId(media.contentUri, media.id)
    }

    fun isVideo():Boolean{
        return media.mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
    }

    fun isImage():Boolean{
        return media.mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
    }

    fun getId() = media.id
}