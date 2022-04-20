package com.example.suwon_university_community.ui.main.memo.folder.timetablememolist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.suwon_university_community.data.entity.memo.FolderEntity
import com.example.suwon_university_community.data.repository.memo.MemoRepository
import com.example.suwon_university_community.data.repository.timetable.TimeTableRepository
import com.example.suwon_university_community.model.MemoModel
import com.example.suwon_university_community.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


class TimeTableMemoListViewModel @Inject constructor(
    private val memoRepository: MemoRepository,
    private val timeTableRepository: TimeTableRepository
) : BaseViewModel() {

    var folderId = 0L
    val timetableMemoListStateLiveData =
        MutableLiveData<TimeTableMemoListState>(TimeTableMemoListState.Uninitialized)

    lateinit var folder: FolderEntity

    override fun fetchData(): Job = viewModelScope.launch {

        timetableMemoListStateLiveData.value = TimeTableMemoListState.Loading

        folder = memoRepository.getFolder(folderId)
        folder.timeTableId?.let { tableId ->
            val timeTableWithCell = timeTableRepository.getTimeTableWithCell(tableId)

            val memos = memoRepository.getFolderWithMemo(folderId).memos.map { memo ->
                memo.toModel()
            }


            timetableMemoListStateLiveData.value =
                TimeTableMemoListState.Success(timeTableWithCell, memos)
        } ?: kotlin.run {
            val timeTableList = timeTableRepository.getTimeTableList()
            timetableMemoListStateLiveData.value = TimeTableMemoListState.NoTimeTable(timeTableList)
        }
    }


    fun updateFolderWithTimeTable(timeTableId: Long) = viewModelScope.launch {
        if (::folder.isInitialized) {
            memoRepository.updateFolder(
                FolderEntity(
                    folderId,
                    name = folder.name,
                    count = folder.count,
                    category = folder.category,
                    isDefault = folder.isDefault,
                    timeTableId = timeTableId
                )
            )
        }
        fetchData()
    }

    fun replaceMemo(memoId: Long, timeTableCellId: Long?) = viewModelScope.launch {
        val memo = memoRepository.getMemo(memoId)
        val updateMemo = memo.copy(timeTableCellId = timeTableCellId)
        memoRepository.updateMemo(updateMemo)

        when (val data = timetableMemoListStateLiveData.value) {
            is TimeTableMemoListState.Success -> {
                val db = data.memoList.toMutableList().apply {
                    remove(memo.toModel())
                    add(updateMemo.toModel())
                }

                timetableMemoListStateLiveData.value = TimeTableMemoListState.EditMemo(db)
            }


            is TimeTableMemoListState.EditMemo -> {
                val db = data.memoList.toMutableList().apply {
                    remove(memo.toModel())
                    add(updateMemo.toModel())
                }

                timetableMemoListStateLiveData.value = TimeTableMemoListState.EditMemo(db)
            }
        }
    }


    fun deleteMemo(model: MemoModel) = viewModelScope.launch {
        memoRepository.deleteMemo(model)

        when (val data = timetableMemoListStateLiveData.value) {
            is TimeTableMemoListState.Success -> {
                val db = data.memoList.toMutableList().apply {
                    remove(model)
                }

                timetableMemoListStateLiveData.value = TimeTableMemoListState.EditMemo(db)
            }

            is TimeTableMemoListState.EditMemo -> {
                val db = data.memoList.toMutableList().apply {
                    remove(model)
                }

                timetableMemoListStateLiveData.value = TimeTableMemoListState.EditMemo(db)
            }
        }
    }

    fun changeFolder(model: MemoModel, folderId: Long) = viewModelScope.launch {
        memoRepository.changeFolder(model, folderId)

        when (val data = timetableMemoListStateLiveData.value) {
            is TimeTableMemoListState.Success -> {
                val db = data.memoList.toMutableList().apply {
                    remove(model)
                }

                timetableMemoListStateLiveData.value = TimeTableMemoListState.EditMemo(db)
            }

            is TimeTableMemoListState.EditMemo -> {
                val db = data.memoList.toMutableList().apply {
                    remove(model)
                }

                timetableMemoListStateLiveData.value = TimeTableMemoListState.EditMemo(db)
            }
        }
    }
}