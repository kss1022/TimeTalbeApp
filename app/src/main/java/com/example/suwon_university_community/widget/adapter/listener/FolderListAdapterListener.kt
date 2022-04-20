package com.example.suwon_university_community.widget.adapter.listener

import com.example.suwon_university_community.model.FolderModel

interface FolderListAdapterListener : AdapterListener {
    fun selectFolder( model: FolderModel)

    fun selectEdit(model : FolderModel)

    fun selectDelete(model : FolderModel)
}