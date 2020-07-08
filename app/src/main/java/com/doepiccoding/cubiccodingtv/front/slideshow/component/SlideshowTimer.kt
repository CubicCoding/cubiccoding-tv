package com.doepiccoding.cubiccodingtv.front.slideshow.component

import android.os.Handler
import androidx.fragment.app.Fragment
import com.doepiccoding.cubiccodingtv.model.pubsub.Pubsub
import com.doepiccoding.cubiccodingtv.model.pubsub.PubsubEvents
import java.lang.ref.WeakReference

class SlideshowTimer(fragment: Fragment) {

    private val weakFragment = WeakReference(fragment)
    private var slideShowHandler: Handler? = null
    private var slideShowRunner: Runnable? = null
    private var isTimerRunning = false

    fun startTimeUp(delay: Long) {
        //Init the timer components...
        slideShowHandler = Handler()
        slideShowRunner = Runnable {
            isTimerRunning = false
            weakFragment.get()?.activity?.supportFragmentManager?.popBackStack()
            Pubsub.INSTANCE.publish(Pubsub.PubsubData(PubsubEvents.FETCH_NEXT_CONTENT_SLIDESHOW, null))
        }

        //Start the delay...
        isTimerRunning = true
        slideShowHandler?.postDelayed(slideShowRunner, delay)
    }

    fun isRunning() = isTimerRunning

    fun release() {
        slideShowHandler?.removeCallbacks(slideShowRunner)
        slideShowRunner = null
        slideShowHandler = null
        isTimerRunning = false
    }
}
