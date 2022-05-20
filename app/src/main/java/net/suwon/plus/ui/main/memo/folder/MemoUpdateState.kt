package net.suwon.plus.ui.main.memo.folder

sealed class MemoUpdateState{
    data class DELETE(val id : Long) : MemoUpdateState()
    data class FINISH(val id : Long) :MemoUpdateState()
}
