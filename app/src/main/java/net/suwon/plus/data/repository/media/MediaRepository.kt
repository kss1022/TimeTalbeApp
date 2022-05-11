package net.suwon.plus.data.repository.media


import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import net.suwon.plus.data.entity.media.Media

interface MediaRepository {

    fun getItems(
        selectionType: MediaPagingSource.SelectionType,
        bucketId: Long?,
        startPosition: Int,
        pageSize: Int
    ): Flow<PagingData<Media>>

    fun invalidate()


    fun getCount(): LiveData<Int>
}