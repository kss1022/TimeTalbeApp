package net.suwon.plus.util

import android.graphics.Canvas
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import net.suwon.plus.R

class SwipeHelperCallback(private var clamp : Float = 400f)  :  ItemTouchHelper.Callback() {

    private val tag : Boolean = false
    private var currentPosition: Int? = null
    private var previousPosition: Int? = null
    private var currentDx = 0f
    //private var clamp = 400f


    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }


    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        currentDx = 0f
        previousPosition = viewHolder.adapterPosition

        getDefaultUIUtil().clearView(getView(viewHolder))
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.let {
            currentPosition = viewHolder.adapterPosition
            getDefaultUIUtil().onSelected(getView(it))
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val view = getView(viewHolder)
            val isClamped = getTag(viewHolder)
            val x = clampViewPositionHorizontal(view, dX, isClamped, isCurrentlyActive)


            val position = viewHolder.adapterPosition
            val v = recyclerView.layoutManager!!.findViewByPosition(position)

            val buttonContainer: LinearLayout = v!!.findViewById(R.id.buttonContainer)
            val lp = buttonContainer.layoutParams

            if (x == 0f) {
                lp.width = 0
            } else {
                lp.width = (-x).toInt()
            }

            buttonContainer.layoutParams = lp


            currentDx = x
            getDefaultUIUtil().onDraw(
                c,
                recyclerView,
                view,
                x,
                dY,
                actionState,
                isCurrentlyActive
            )
        }
    }

    //???????????? ??? ?????? ??????
    private fun getView(viewHolder: RecyclerView.ViewHolder): View {
        return (viewHolder).itemView.findViewById(R.id.swipeView)
    }

    // ???????????? view??? swipe ????????? ????????? ?????? ?????? ??????
    override fun getSwipeEscapeVelocity(defaultValue: Float): Float = defaultValue * 10


    //???????????? ???????????? ????????????.
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        val isClamped = getTag(viewHolder)
        // ?????? View??? ?????????????????? ??????
        // ???????????? -clamp ?????? swipe??? isClamped true??? ?????? ????????? false??? ??????
        setTag(viewHolder, !isClamped && currentDx <= -clamp)
        return 2f
    }


    private fun clampViewPositionHorizontal(
        view: View,
        dX: Float,
        isClamped: Boolean,
        isCurrentlyActive: Boolean
    ): Float {
        // View??? ?????? ????????? ??????????????? swipe ?????????
        val min: Float = -view.width.toFloat() / 2
        // RIGHT ???????????? swipe ??????
        val max: Float = 0f

        val x = if (isClamped) {
            // View??? ??????????????? ??? swipe?????? ?????? ??????
            if (isCurrentlyActive) dX - clamp else -clamp
        } else {
            dX
        }

        return Math.min(Math.max(min, x), max)
    }

    // isClamped??? view??? tag??? ??????
    private fun setTag(viewHolder: RecyclerView.ViewHolder, isClamped: Boolean) {
        viewHolder.itemView.tag = isClamped
    }

    private fun getTag(viewHolder: RecyclerView.ViewHolder): Boolean =
        viewHolder.itemView.tag as? Boolean ?: false


    // ?????? View??? swipe ????????? ???????????? ?????? ??????
    fun removePreviousClamp(recyclerView: RecyclerView) {

        //  if (currentPosition == previousPosition) return
        previousPosition?.let {

            val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return


            val position = viewHolder.adapterPosition
            val v = recyclerView.layoutManager!!.findViewByPosition(position)
            val deleteButton: LinearLayout = v!!.findViewById(R.id.buttonContainer)
            val deleteLp = deleteButton.layoutParams
            deleteLp.width = 0
            deleteButton.layoutParams = deleteLp

            getView(viewHolder).translationX = 0f
            setTag(viewHolder, false)
            previousPosition = null
        }
    }
}