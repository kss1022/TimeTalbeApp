package net.suwon.plus.data.entity.memo

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.suwon.plus.model.MemoModel

@Entity
data class MemoEntity (
    @PrimaryKey(autoGenerate = true)
    val memoId : Long = 0,
    val title : String,
    val memo : String,
    val time : Long = System.currentTimeMillis(),
    val memoFolderId : Long,
    val timeTableCellId : Long? = null
) {
    fun toModel(): MemoModel = MemoModel(
        id = memoId,
        title = title,
        memo = memo,
        time= time,
        memoFolderId =memoFolderId,
        timeTableCellId = timeTableCellId,
    )
}