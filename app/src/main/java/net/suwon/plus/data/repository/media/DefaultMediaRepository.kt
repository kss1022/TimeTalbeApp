package net.suwon.plus.data.repository.media

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import net.suwon.plus.data.entity.media.Media
import net.suwon.plus.util.PagingConstants

class DefaultMediaRepository(
    private val context : Context
) : MediaRepository {

    private var currentPagingSource: MediaPagingSource? = null

    private val count = MutableLiveData<Int>()

    override fun getItems(
        selectionType: MediaPagingSource.SelectionType,
        bucketId: Long?,
        startPosition: Int,
        pageSize: Int
    ): Flow<PagingData<Media>> {

        return Pager(
            PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = true,
                initialLoadSize = PagingConstants.DEFAULT_PAGE_SIZE
            ),
            initialKey = startPosition
        ) {
            MediaPagingSource(
                context = context,
                bucketId = bucketId,
                selectionType = selectionType,
                count = count
            ).also {
                currentPagingSource = it
            }
        }.flow
    }

    override fun invalidate(){
        currentPagingSource?.invalidate()
    }


    override fun getCount(): LiveData<Int> = count
}