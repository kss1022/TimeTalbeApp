package net.suwon.plus.ui.main.memo.folder.editmemo.gallery.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.suwon.plus.data.entity.media.MediaItem
import net.suwon.plus.util.lifecycle.SingleLiveEvent
import javax.inject.Inject

class MediaDetailViewModel @Inject constructor(
) : ViewModel() {

    val checkBoxEnabled = MutableLiveData(true)
    val isChecked = MutableLiveData<Boolean>()
    var currentMediaItem: MediaItem? = null
    val checkBoxClickEvent = SingleLiveEvent<MediaItem?>()

    fun onCheckBoxClick() {
        checkBoxClickEvent.value = currentMediaItem
    }
}