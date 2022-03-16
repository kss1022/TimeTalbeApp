package com.example.suwon_university_community.ui.main.home

import androidx.lifecycle.viewModelScope
import com.example.suwon_university_community.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel  @Inject constructor() : BaseViewModel() {

    override fun fetchData(): Job = viewModelScope.launch{
    }
}