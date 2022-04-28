package net.suwon.plus.widget.adapter.listener

import net.suwon.plus.model.MemoModel

interface MemoListAdapterListener : AdapterListener {
    fun selectModel(model : MemoModel)

    fun selectEdit(model : MemoModel)

    fun selectDelete(model : MemoModel)
}