package net.suwon.plus.data.entity.memo

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import net.suwon.plus.model.MemoModel
import net.suwon.plus.util.converter.RoomTypeConverter

@Entity
@TypeConverters(RoomTypeConverter::class)
data class MemoEntity (
    @PrimaryKey(autoGenerate = true)
    val memoId : Long = 0,
    val title : String,
    val memo : String,
    val time : Long = System.currentTimeMillis(),
    val memoFolderId : Long,
    val timeTableCellId : Long? = null,
    val imageUrlList : List<String> = listOf()
) {
    fun toModel(): MemoModel = MemoModel(
        id = memoId,
        title = title,
        memo = memo,
        time= time,
        memoFolderId =memoFolderId,
        timeTableCellId = timeTableCellId,
//        imageUrlList =  imageUrlList
    )
}