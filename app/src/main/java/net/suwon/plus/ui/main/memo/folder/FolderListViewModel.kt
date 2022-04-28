package net.suwon.plus.ui.main.memo.folder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.suwon.plus.data.entity.memo.FolderCategory
import net.suwon.plus.data.entity.memo.FolderEntity
import net.suwon.plus.data.repository.memo.MemoRepository
import net.suwon.plus.data.repository.timetable.TimeTableRepository
import net.suwon.plus.model.FolderModel
import net.suwon.plus.ui.base.BaseViewModel
import javax.inject.Inject



class FolderListViewModel @Inject constructor(
    private val memoRepository: MemoRepository,
    private val timeTableRepository: TimeTableRepository
) : BaseViewModel() {

    val memoStateLiveData = MutableLiveData<FolderListState>(FolderListState.Uninitialized)
    val timeTableCountLiveData = MutableLiveData<Pair<FolderModel, Int>>()
    val folders: MutableStateFlow<List<FolderEntity>> = MutableStateFlow(emptyList())

    override fun fetchData(): Job = viewModelScope.launch {
        memoStateLiveData.value = FolderListState.Loading
        memoRepository.getFolderList().onEach { folders.value = it }.launchIn(this)
        memoStateLiveData.value = FolderListState.Success
    }

    fun addFolder(folderName: String, folderCategory: FolderCategory) = viewModelScope.launch {
        memoRepository.insertFolder(
            FolderEntity(
                name = folderName,
                category = folderCategory
            )
        )
    }

    fun checkTimeTableCount(model: FolderModel) = viewModelScope.launch {
        timeTableRepository.getTimeTableCount()?.let {
            timeTableCountLiveData.value = model to it
        }
    }

    fun deleteFolder(model: FolderModel) = viewModelScope.launch {
        memoRepository.deleteFolder(model.id)
    }

    fun changeFolderName(model: FolderModel, name: String) = viewModelScope.launch{
        val updateFolder =
            FolderEntity(
                folderId = model.id,
                name = name,
                count = model.count,
                category = model.category,
                isDefault = model.isDefault,
                timeTableId = model.timeTableId
            )

        memoRepository.updateFolder(updateFolder)
    }
}