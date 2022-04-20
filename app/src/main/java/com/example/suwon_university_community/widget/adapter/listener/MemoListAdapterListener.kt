package com.example.suwon_university_community.widget.adapter.listener

import com.example.suwon_university_community.model.MemoModel

interface MemoListAdapterListener : AdapterListener {
    fun selectModel(model : MemoModel)

    fun selectEdit(model : MemoModel)

    fun selectDelete(model : MemoModel)
}