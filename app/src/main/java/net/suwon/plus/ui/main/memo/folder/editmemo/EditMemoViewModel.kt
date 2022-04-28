package net.suwon.plus.ui.main.memo.folder.editmemo

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.suwon.plus.data.entity.memo.MemoEntity
import net.suwon.plus.data.repository.memo.MemoRepository
import net.suwon.plus.ui.base.BaseViewModel
import javax.inject.Inject

class EditMemoViewModel @Inject constructor(
    private val memoRepository: MemoRepository
) : BaseViewModel() {

    override fun fetchData(): Job  = viewModelScope.launch {  }

    fun insertMemo(memoEntity: MemoEntity) = viewModelScope.launch {
        memoRepository.insertMemo(memoEntity)
    }


    fun updateMemo( memoEntity: MemoEntity) = viewModelScope.launch {
        memoRepository.updateMemo(memoEntity)
    }
}