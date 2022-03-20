package com.example.suwon_university_community.extensions


import android.content.res.Resources

internal fun Float.fromDpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}

internal fun Int.fromDpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}