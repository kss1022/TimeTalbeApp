package net.suwon.plus.widget.adapter.listener

import net.suwon.plus.model.FolderModel

interface FolderListAdapterListener : AdapterListener {
    fun selectFolder( model: FolderModel)

    fun selectEdit(model : FolderModel)

    fun selectDelete(model : FolderModel)
}