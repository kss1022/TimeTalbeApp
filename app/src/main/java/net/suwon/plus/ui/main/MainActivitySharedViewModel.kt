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
    private val memoRepository: MemoRepository
) : BaseViewModel() {

    private lateinit var imageUrlList: MutableList<String>

    private var saveImageList = mutableListOf<Media>()
    private var deleteImageList = mutableListOf<String>()
    val memoUpdateLiveData = SingleLiveEvent<Long>()


    fun updateMemo( memoEntity: MemoEntity) = viewModelScope.launch {
        memoRepository.updateMemo(memoEntity)
        memoUpdateLiveData.value = memoEntity.memoId
    }

    fun deleteMemo(id: Long) = viewModelScope.launch{
        memoRepository.deleteMemo(id)
        memoUpdateLiveData.value = -1
    }

    fun invalidMemo(id: Long, list: MutableList<String>) = viewModelScope.launch{
        if(id == -1L) return@launch
        if(saveImageList.isEmpty() && deleteImageList.isEmpty()) return@launch

        val memo = memoRepository.getMemo(id)
        val defaultUrlList = memo.imageUrlList.toMutableList()


        memoRepository.updateMemo(memo.copy(imageUrlList = list))
        memoUpdateLiveData.value = id


        deleteImageList.forEach { delete->
            val findDefault = defaultUrlList.find { it == delete }
            val findSave = saveImageList.find { it.getUri().toString() == delete }


            findDefault?.let {
                File(findDefault).delete()
                defaultUrlList.remove(findDefault)
            }
            findSave?.let{ saveImageList.remove(findSave)
            }
        }

        imageUrlList = mutableListOf()
        withContext(Dispatchers.IO){
            saveImageList.forEach { media ->
                saveFile(media.getUri(), media.name)
            }
        }

        imageUrlList.forEach {
            defaultUrlList.add(it)
        }

        memoRepository.updateMemo(memo.copy(imageUrlList = defaultUrlList))
        saveImageList = mutableListOf()
        deleteImageList = mutableListOf()
        memoUpdateLiveData.value = id
    }



    fun setSaveImageList(mediaArray: ArrayList<Media>) = viewModelScope.launch {
        saveImageList = mediaArray

    }


    fun setDeleteImageList(mediaArray: ArrayList<String>) = viewModelScope.launch {
        deleteImageList = mediaArray
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
            val file = createExternalFolder()
            try {
                val imageFile = File(file, "${name}.${System.currentTimeMillis()}.png")

                val stream = FileOutputStream(imageFile)
                bimMap.compress(Bitmap.CompressFormat.PNG, 100, stream)

                stream.flush()
                stream.close()

                imageUrlList.add(imageFile.absolutePath)
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
        val file =
            File(app.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "/memo")

        if (!file.exists()) {
            file.mkdirs();
        }

        return file
    }


}