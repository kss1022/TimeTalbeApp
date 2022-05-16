package net.suwon.plus.ui.main.memo.folder.editmemo

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.suwon.plus.data.entity.memo.MemoEntity
import net.suwon.plus.data.repository.memo.MemoRepository
import net.suwon.plus.model.MemoModel
import net.suwon.plus.ui.base.BaseViewModel
import net.suwon.plus.util.lifecycle.SingleLiveEvent
import javax.inject.Inject

class EditMemoViewModel @Inject constructor(
    private val memoRepository: MemoRepository
) : BaseViewModel() {

    lateinit var memo : MemoModel
    val insertMemoState = SingleLiveEvent<MemoModel>()


    override fun fetchData(): Job  = viewModelScope.launch {
        if(memo.id == -1L){
            insertMemo(
                MemoEntity(
                    title = "",
                    memo = "",
                    time = System.currentTimeMillis(),
                    memoFolderId = memo.memoFolderId,
                    timeTableCellId = memo.timeTableCellId,
                )
            )
        }
    }



    private fun insertMemo(memoEntity: MemoEntity) = viewModelScope.launch {
        val memoId = memoRepository.insertMemo(memoEntity)
        insertMemoState.value = memoEntity.copy(memoId = memoId).toModel()
    }
}