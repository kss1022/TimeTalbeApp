package net.suwon.plus.ui.main.memo.folder.editmemo.gallery

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import net.suwon.plus.data.entity.media.Album
import net.suwon.plus.data.entity.media.Media
import net.suwon.plus.data.entity.media.MediaItem
import net.suwon.plus.data.entity.media.Selection
import net.suwon.plus.data.repository.media.DefaultMediaRepository
import net.suwon.plus.data.repository.media.MediaPagingSource
import net.suwon.plus.data.repository.media.MediaRepository
import net.suwon.plus.util.PagingConstants
import net.suwon.plus.util.lifecycle.SingleLiveEvent
import java.io.IOException
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class GallerySharedViewModel @Inject constructor(
    private val app : Application,
) : AndroidViewModel(app) , DefaultLifecycleObserver{

    val repository: MediaRepository = DefaultMediaRepository(app)
    val selection: Selection =  Selection()
    val currentFolder = MutableLiveData<Album>(null)
    val itemCount = repository.getCount()

    val itemClickEvent = SingleLiveEvent<Triple<View, Media, Int>?>()
    val bindingItemAdapterPosition = AtomicInteger(PagingConstants.NO_POSITION)


    //flatMapLastest 최신데이터만 이용
    val items: Flow<PagingData<MediaItem>> = currentFolder
        .asFlow()
        .flatMapLatest { album: Album? ->
            repository.getItems(
                MediaPagingSource.SelectionType.IMAGE_AND_VIDEO,
                album?.bucketId,
                PagingConstants.DEFAULT_POSITION,
                PagingConstants.DEFAULT_PAGE_SIZE
            ).map { pagingData ->
                pagingData.map { media ->
                    MediaItem(media)
                }
            }
        }
        .cachedIn(viewModelScope)

    fun setBucketId(album: Album) {
        currentFolder.value = album
    }

    fun onItemClick(v: View, item: MediaItem, position: Int) {
        itemClickEvent.value = Triple(v, item.media, position)
    }

    fun onCheckBoxClick(item: MediaItem) {
        selection.toggle(item.getId(), item.media)
    }



    fun getSelectedMediaList(): List<Media> {
        return selection.toList()
            .filter { media -> validateIfExist(media.getUri()) }
    }

    private fun validateIfExist(uri: Uri): Boolean {
        return try {
            app.contentResolver.openInputStream(uri)?.use { it.close() }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
}