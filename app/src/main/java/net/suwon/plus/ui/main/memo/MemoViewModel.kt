package net.suwon.plus.ui.main.memo

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.suwon.plus.ui.base.BaseViewModel
import javax.inject.Inject

class MemoViewModel  @Inject constructor(
) : BaseViewModel() {


    override fun fetchData(): Job = viewModelScope.launch{}
}