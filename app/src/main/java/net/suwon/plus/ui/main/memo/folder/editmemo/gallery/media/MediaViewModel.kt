package net.suwon.plus.ui.main.memo.folder.editmemo.gallery.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


class MediaViewModel @Inject constructor(
) : ViewModel() {

    fun fetchData(): Job = viewModelScope.launch {
    }

}