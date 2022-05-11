package net.suwon.plus.util

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import android.view.Window
import androidx.annotation.DimenRes
import kotlin.math.max


object MeasureUtil {
    fun getSpanCount(context: Context, columnWidthPx: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.let {
            val widthPixels = it.widthPixels.toFloat()
            return (widthPixels / columnWidthPx).toInt()
        }
    }

    fun getProperSpanCount(
        context: Context,
        @DimenRes columnWidthDimen: Int,
        minSpan: Int = 1
    ): Int {
        val columnWidthPx = context.resources.getDimensionPixelSize(columnWidthDimen)
        return max(getSpanCount(context, columnWidthPx), minSpan)
    }

    fun dpToPx(context: Context, dp: Float): Int {
        val value = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        )
        return value.toInt()
    }

    fun getToolBarHeight(context: Context): Int {
        val attrs = intArrayOf(android.R.attr.actionBarSize)
        val ta: TypedArray = context.obtainStyledAttributes(attrs)
        val toolBarHeight = ta.getDimensionPixelSize(0, -1)
        ta.recycle()
        return toolBarHeight
    }

    fun getStatusBarHeight(window: Window): Int {
        val rectangle = Rect()
        window.decorView.getWindowVisibleDisplayFrame(rectangle)
        val statusBarHeight: Int = rectangle.top
        val contentView = window.findViewById<View>(Window.ID_ANDROID_CONTENT)
        val contentViewTop: Int = contentView.top
        return statusBarHeight
    }

    fun getNavigationBarHeight(context: Context): Int {
        val resources: Resources = context.resources
        val resourceId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    fun getDimension(context:Context, resId:Int):Float{
        return context.resources.getDimension(resId)
    }
}

