package com.example.suwon_university_community.widget.adapter.listener

import com.example.suwon_university_community.model.NoticeModel

interface NoticeListAdapterListener : AdapterListener {
    fun selectNotice(model : NoticeModel)
}