package net.suwon.plus.ui.main.home

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.suwon.plus.ui.base.BaseViewModel
import javax.inject.Inject

class HomeViewModel  @Inject constructor(
) : BaseViewModel() {


    override fun fetchData(): Job = viewModelScope.launch{
    }
}