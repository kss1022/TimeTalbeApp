package net.suwon.plus.ui.main.memo.folder.timetablememolist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.suwon.plus.data.entity.memo.FolderEntity
import net.suwon.plus.data.repository.memo.MemoRepository
import net.suwon.plus.data.repository.timetable.TimeTableRepository
import net.suwon.plus.model.MemoModel
import net.suwon.plus.ui.base.BaseViewModel
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
                TimeTableMemoListState.Success(
                    timeTableWithCell,
                    memos.sortedByDescending { it.time })
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


    fun updateMemo(memoId: Long, isDelete: Boolean = false) = viewModelScope.launch {
        if (memoId == -1L || isDelete) {
            when (val data = timetableMemoListStateLiveData.value) {
                is TimeTableMemoListState.Success -> {
                    val db = data.memoList.toMutableList()
                    for (i in 0 until data.memoList.size) {
                        if (db[i].id == memoId) {
                            db.removeAt(i)
                            timetableMemoListStateLiveData.value =
                                TimeTableMemoListState.EditMemo(db)
                            return@launch
                        }
                    }
                }


                is TimeTableMemoListState.EditMemo -> {
                    val db = data.memoList.toMutableList()
                    for (i in 0 until data.memoList.size) {
                        if (db[i].id == memoId) {
                            db.removeAt(i)
                            timetableMemoListStateLiveData.value =
                                TimeTableMemoListState.EditMemo(db)
                            return@launch
                        }
                    }
                }
            }
            return@launch
        }

        val memo = memoRepository.getMemo(memoId).toModel()

        when (val data = timetableMemoListStateLiveData.value) {
            is TimeTableMemoListState.Success -> {
                val db = data.memoList.toMutableList()
                for (i in 0 until data.memoList.size) {
                    if (data.memoList[i].id == memoId) {
                        db[i] = memo
                    }
                }

                timetableMemoListStateLiveData.value = TimeTableMemoListState.EditMemo(db)
            }


            is TimeTableMemoListState.EditMemo -> {
                val db = data.memoList.toMutableList().apply {
                    for (i in 0 until data.memoList.size) {
                        if (data.memoList[i].id == memoId) {
                            set(i, memo)
                        }
                    }
                }
                timetableMemoListStateLiveData.value = TimeTableMemoListState.EditMemo(db)
            }
        }
    }

    // SharedViewModel 의 값에 따른 변경  ( Adapter 의 값만 바꿔준다)
    fun replaceMemo(memoId: Long, timeTableCellId: Long?) = viewModelScope.launch {
        val memo = memoRepository.getMemo(memoId)
        val updateMemo = memo.copy(timeTableCellId = timeTableCellId)
        memoRepository.updateMemo(updateMemo)

        when (val data = timetableMemoListStateLiveData.value) {
            is TimeTableMemoListState.Success -> {
                val db = data.memoList.toMutableList()

                for (i in 0 until data.memoList.size) {
                    if (db[i].id == memoId) {
                        db[i] = updateMemo.toModel()
                        timetableMemoListStateLiveData.value = TimeTableMemoListState.EditMemo(db)
                        return@launch
                    }
                }

            }


            is TimeTableMemoListState.EditMemo -> {
                val db = data.memoList.toMutableList()
                for (i in 0 until data.memoList.size) {
                    if (db[i].id == memoId) {
                        db[i] = updateMemo.toModel()
                        timetableMemoListStateLiveData.value = TimeTableMemoListState.EditMemo(db)
                        return@launch
                    }
                }
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

            else -> Unit
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