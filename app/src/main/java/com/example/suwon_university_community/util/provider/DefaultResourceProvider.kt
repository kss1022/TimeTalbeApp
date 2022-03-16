package com.example.suwon_university_community.util.provider

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class DefaultResourceProvider(
    private val context: Context
) : ResourceProvider {
    override fun getString(resId: Int): String = context.getString(resId)

    override fun getString(resId: Int, vararg formArgs: Any): String = context.getString(resId, *formArgs)

    override fun getColor(@ColorRes resId: Int): Int = ContextCompat.getColor(context, resId)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun getColorStateList(@ColorRes resId: Int): ColorStateList = context.getColorStateList(resId)

}