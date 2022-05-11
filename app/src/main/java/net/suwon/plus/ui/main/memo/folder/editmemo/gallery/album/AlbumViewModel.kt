package net.suwon.plus.ui.main.memo.folder.editmemo.gallery.album

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import net.suwon.plus.data.entity.media.AlbumItem
import net.suwon.plus.data.repository.media.DefaultAlbumRepository
import javax.inject.Inject


class AlbumViewModel @Inject constructor(
    private val app : Application,
 ) : AndroidViewModel(app) {

    val items = MutableLiveData<List<AlbumItem>>()
    val  repositoryDefault = DefaultAlbumRepository(app)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryDefault.load().map { map ->
                map.values.toList()
                    .sortedByDescending { folder -> folder.order }
                    .map { folder ->
                        AlbumItem(folder)
                    }
            }.collectLatest {
                items.postValue(it)
            }
        }
    }


    fun fetchData() : Job = viewModelScope.launch {  }


}
