package com.doepiccoding.cubiccodingtv.front.home.score.components

import android.content.Context
import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import java.lang.ref.WeakReference
import java.text.FieldPosition

class BouncingScroll(context: Context?, totalItems: Int, layoutManager: LinearLayoutManager) {

    companion object {
        const val DELAY_ON_EACH_CARD_MS = 5000L
    }
    private val weakLayoutManager = WeakReference(layoutManager)
    private var runnerHandler: Handler? = Handler()
    private var currentDirection = Direction.TOP_TO_BOTTOM

    private enum class Direction {
        TOP_TO_BOTTOM,
        BOTTOM_TO_TOP
    }

    private var smoothScroller: RecyclerView.SmoothScroller? = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
    private val nextItemRunnable = object : Runnable {
        override fun run() {
            //Check for the right position to scroll to...
            val tmpLayoutManager = weakLayoutManager.get()
            if (tmpLayoutManager != null) {
                val nextPositionToScrollTo = calculateNextPossiblePosition(tmpLayoutManager.findLastCompletelyVisibleItemPosition(), tmpLayoutManager.findFirstCompletelyVisibleItemPosition(), totalItems)
                Timber.e("Track, NExt Possible position: $nextPositionToScrollTo")
                applyScrolling(nextPositionToScrollTo, tmpLayoutManager)
                figureOutDirection(nextPositionToScrollTo, totalItems, tmpLayoutManager.findFirstCompletelyVisibleItemPosition())
                runnerHandler?.postDelayed(this, DELAY_ON_EACH_CARD_MS)
            } else {
                release()
            }
        }
    }

    fun calculateNextPossiblePosition(lastCompletelyVisibleItemPosition: Int, firstCompletelyVisibleItem: Int, totalItems: Int): Int {
        val nextPossibleItemPosition = if (currentDirection == Direction.TOP_TO_BOTTOM) {
            lastCompletelyVisibleItemPosition + 1
        } else {
            firstCompletelyVisibleItem - 1
        }

        //Sanitize the position for edge cases where possible position may land out of range...
        return if (nextPossibleItemPosition < 0 || nextPossibleItemPosition >= totalItems)  lastCompletelyVisibleItemPosition else nextPossibleItemPosition
    }

    fun figureOutDirection(nextPositionToScrollTo: Int, totalItems: Int, firstCompletelyVisibleItem: Int) {
        //Figure out direction
        if (currentDirection == Direction.TOP_TO_BOTTOM && nextPositionToScrollTo >= totalItems - 1) {
            currentDirection = Direction.BOTTOM_TO_TOP
            Timber.e("Track, Move bottom to top now...")
        }
        if (currentDirection == Direction.BOTTOM_TO_TOP && firstCompletelyVisibleItem == 0) {
            currentDirection = Direction.TOP_TO_BOTTOM
            Timber.e("Track, Go top to bottom...")
        }
    }

    private fun applyScrolling (position: Int, linearLayoutManager: LinearLayoutManager) {
        val tmpSmoothScroller = smoothScroller
        if (tmpSmoothScroller != null) {
            tmpSmoothScroller.targetPosition = position
            linearLayoutManager.startSmoothScroll(tmpSmoothScroller)
        } else {
            linearLayoutManager.scrollToPosition(position)
        }
    }

    fun start() {
        runnerHandler?.postDelayed(nextItemRunnable, DELAY_ON_EACH_CARD_MS)
    }

    fun release() {
        Timber.e("Track, Released...")
        runnerHandler?.removeCallbacks(nextItemRunnable)
        runnerHandler = null
        smoothScroller = null
    }

}