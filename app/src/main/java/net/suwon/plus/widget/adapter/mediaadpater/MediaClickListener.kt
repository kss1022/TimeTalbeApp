package net.suwon.plus.widget.adapter.mediaadpater

import android.view.View
import net.suwon.plus.data.entity.media.MediaItem


interface MediaClickListener {

    fun itemClick(view : View, item: MediaItem, position: Int)
    fun checkBoxClick(item: MediaItem)
}