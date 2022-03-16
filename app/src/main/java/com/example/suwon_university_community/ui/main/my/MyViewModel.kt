package com.example.suwon_university_community.ui.main.my

import androidx.lifecycle.viewModelScope
import com.example.suwon_university_community.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class MyViewModel  @Inject constructor() : BaseViewModel() {

    override fun fetchData(): Job = viewModelScope.launch{
    }
}