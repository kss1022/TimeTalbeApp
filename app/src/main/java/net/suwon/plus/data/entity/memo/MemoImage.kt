package net.suwon.plus.data.entity.memo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class MemoImage(
    val name : String,
    val url : String?,
    val isSaved : Boolean = false
):Parcelable
