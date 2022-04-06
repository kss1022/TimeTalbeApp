package com.example.suwon_university_community.data.api

import com.example.suwon_university_community.data.api.response.notice.NoticeApi
import com.example.suwon_university_community.data.entity.notice.NoticeEntity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class JsonNoticeService @Inject constructor(
    private val firebaseStore: FirebaseStorage,
    private val noticeApi: NoticeApi
) : NoticeService {

    private val sheetReference: StorageReference =
        firebaseStore.reference.child(NOTICE_FILE_NAME)


    override suspend fun getUpdatedTimeMillis(): Long =
        sheetReference.metadata.await().updatedTimeMillis

    override suspend fun getNoticeList(): List<NoticeEntity> =
        try{
            val url =  sheetReference.downloadUrl.await()


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


