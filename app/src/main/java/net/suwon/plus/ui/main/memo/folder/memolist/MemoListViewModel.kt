package net.suwon.plus.ui.main.memo.folder.memolist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.suwon.plus.data.repository.memo.MemoRepository
import net.suwon.plus.model.MemoModel
import net.suwon.plus.ui.base.BaseViewModel
import javax.inject.Inject

class MemoListViewModel @Inject constructor(
    private val memoRepository: MemoRepository
) : BaseViewModel() {

    var folderId = 0L

    val memoListStateLiveData = MutableLiveData<MemoListState>(MemoListState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        memoListStateLiveData.value = MemoListState.Loading


        val memoList = memoRepository.getFolderWithMemo(folderId).memos.map { memo ->
            memo.toModel()
        }

        memoListStateLiveData.value = MemoListState.Success(memoList.sortedByDescending { it.time })
    }



    fun deleteMemo(model: MemoModel) = viewModelScope.launch {
        memoRepository.deleteMemo(model)

        when (val data = memoListStateLiveData.value) {
            is MemoListState.Success -> {
                memoListStateLiveData.value = data.copy(
                    data.memoList.toMutableList().apply {
                        remove(model)
                    }
                )
            }
        }
    }

    fun changeFolder(model: MemoModel, folderId: Long) = viewModelScope.launch {
        memoRepository.changeFolder(model, folderId)

        when (val data = memoListStateLiveData.value) {
            is MemoListState.Success -> {
                memoListStateLiveData.value = data.copy(
                    data.memoList.toMutableList().apply {
                        remove(model)
                    }
                )
            }
        }
    }



    // SharedViewModel 의 값에 따른 변경  ( Adapter 의 값만 바꿔준다)
    fun updateMemo(memoId: Long, isDelete: Boolean = false) = viewModelScope.launch {
        if (memoId == -1L || isDelete) {
            when (val data = memoListStateLiveData.value) {
                is MemoListState.Success -> {
                    val db = data.memoList.toMutableList()

                    for (i in 0 until data.memoList.size) {
                        if (db[i].id == memoId) {
                            db.removeAt(i)
                            memoListStateLiveData.value = MemoListState.Success(db)
                            return@launch
                        }
                    }
                }
            }
            return@launch
        }

        val memo = memoRepository.getMemo(memoId)

        when (val data = memoListStateLiveData.value) {
            is MemoListState.Success -> {
                val db = data.memoList.toMutableList()
                for (i in 0 until data.memoList.size) {
                    if (db[i].id == memoId) {
                        db[i] = memo.toModel()
                        memoListStateLiveData.value = MemoListState.Success(db)
                        return@launch
                    }
                }
            }
        }
    }
}