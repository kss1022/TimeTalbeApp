package net.suwon.plus.data.api

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import net.suwon.plus.data.api.response.notice.NoticeApi
import net.suwon.plus.data.entity.notice.NoticeEntity
import javax.inject.Inject


class JsonNoticeService @Inject constructor(
    firebaseStore: FirebaseStorage,
    private val noticeApi: NoticeApi
) : net.suwon.plus.data.api.NoticeService {

    private val sheetReference: StorageReference =
        firebaseStore.reference.child(net.suwon.plus.data.api.JsonNoticeService.Companion.NOTICE_FILE_NAME)


    override suspend fun getUpdatedTimeMillis(): Long =
        sheetReference.metadata.await().updatedTimeMillis

    override suspend fun getNoticeList(): List<NoticeEntity> =
        try{
            val response = noticeApi.getNoticeList()

            if(response.isSuccessful){
                response.body()?.map {
                    NoticeEntity(
                        title = it.title ?: "",
                        writer = it.writer ?: "",
                        date = it.date ?: "",
                        url = it.url ?: "",
                        category = it.type ?: ""
                    )
                } ?: listOf()
            } else{
                listOf()
            }
        }catch( e : Exception){
            e.printStackTrace()
            listOf()
        }

    companion object{
        private const val  NOTICE_FILE_NAME = "notice.json"
    }
}


