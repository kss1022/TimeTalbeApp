package net.suwon.plus.widget.adapter.mediaadpater

import net.suwon.plus.data.entity.media.AlbumItem

interface AlbumClickListener {
    fun itemClick(item: AlbumItem)
}