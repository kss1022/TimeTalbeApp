package net.suwon.plus.ui.main

import android.app.Application
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.suwon.plus.data.entity.media.Media
import net.suwon.plus.data.entity.memo.MemoEntity
import net.suwon.plus.data.entity.memo.MemoImage
import net.suwon.plus.data.preference.PreferenceManager
import net.suwon.plus.data.repository.memo.MemoRepository
import net.suwon.plus.ui.base.BaseViewModel
import net.suwon.plus.util.lifecycle.SingleLiveEvent
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject


class MainActivitySharedViewModel @Inject constructor(
    private val app: Application,
    private val memoRepository: MemoRepository,
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {


    val memoUpdateLiveData = SingleLiveEvent<Long>()

    private lateinit var file: File

    fun updateMemo(
        memoEntity: MemoEntity,
        addImageList: MutableList<Media>,
        deleteImageList: MutableList<String>
    ) = viewModelScope.launch {
        memoRepository.updateMemo(memoEntity)
        memoUpdateLiveData.value = memoEntity.memoId

        updateImage(
            memoEntity,
            addImageList,
            deleteImageList
        )
    }


    fun deleteMemo(id: Long) = viewModelScope.launch {
        memoRepository.deleteMemo(id)
        memoUpdateLiveData.value = -1
    }

    private fun updateImage(
        memoEntity: MemoEntity,
        addImageList: MutableList<Media>,
        deleteImageList: MutableList<String>
    ) = viewModelScope.launch {
        if (memoEntity.memoId == -1L) return@launch
        if (addImageList.isEmpty() && deleteImageList.isEmpty()) return@launch

        if (::file.isInitialized.not()) {
            file = createExternalFolder()
        }


        val savedImageList =  memoEntity.imageUrlList.toMutableList()

        if(deleteImageList.isNullOrEmpty().not()){
            deleteImageList.forEach {
                addImageList.forEach { add->
                    if( add.name == it){
                        deleteImageList.remove(it)
                    }
                }
            }

            deleteImageList.forEach {
                File(file.absolutePath +"/"+ it).delete()
            }
        }

        if(savedImageList.isNullOrEmpty().not()){
            withContext(Dispatchers.IO) {
                for(i in 0 until savedImageList.size){
                    if(savedImageList[i].isSaved.not()){
                        saveFile( Uri.parse("${savedImageList[i].url}"),  savedImageList[i].name)
                        savedImageList[i] = MemoImage(savedImageList[i].name , savedImageList[i].url, true)
                    }
                }
            }
        }

        memoRepository.updateMemo(memoEntity.copy(imageUrlList = savedImageList))
        memoUpdateLiveData.value = memoEntity.memoId
    }




    private fun saveFile(uri: Uri, name: String) {
        val bimMap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    app.contentResolver,
                    uri
                )
            )
        } else {
            MediaStore.Images.Media.getBitmap(app.contentResolver, uri)
        }


        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {

            //Create folder if is not exists
            try {
                val imageFile = File(file, name)

                val stream = FileOutputStream(imageFile)
                bimMap.compress(Bitmap.CompressFormat.PNG, 100, stream)

                stream.flush()
                stream.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            Log.e("Save Image", "Directory can't use")
        }

    }


    private fun createExternalFolder(): File {
        val dir = preferenceManager.geFileDir()

        return if (dir == null) {
            val file =
                File(app.getExternalFilesDir(Environment.DIRECTORY_PICTURES), DEFAULT_MEMO_DIR)

            if (!file.exists()) {
                file.mkdirs();
            }
            preferenceManager.putFileDir(file.absolutePath)
            file
        } else {
            File(dir)
        }
    }



    companion object {
        const val DEFAULT_MEMO_DIR = "/memo"
    }

}