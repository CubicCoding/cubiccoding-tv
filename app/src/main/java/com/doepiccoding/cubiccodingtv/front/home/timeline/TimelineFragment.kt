package com.doepiccoding.cubiccodingtv.front.home.timeline

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.doepiccoding.cubiccodingtv.R
import com.doepiccoding.cubiccodingtv.front.home.timeline.model.TimelineRepository
import com.doepiccoding.cubiccodingtv.front.home.timeline.model.TimelineViewModel
import com.doepiccoding.cubiccodingtv.front.home.timeline.recyclerview.TimelineAdapter
import com.doepiccoding.cubiccodingtv.front.slideshow.SlideshowActivity
import com.doepiccoding.cubiccodingtv.front.slideshow.SlideshowActivity.Companion.IS_SLIDESHOW
import com.doepiccoding.cubiccodingtv.front.slideshow.SlideshowActivity.Companion.SLIDE_TIME_UP
import com.doepiccoding.cubiccodingtv.front.slideshow.component.SlideshowTimer
import com.doepiccoding.cubiccodingtv.model.pubsub.Pubsub
import com.doepiccoding.cubiccodingtv.model.pubsub.PubsubEvents
import com.doepiccoding.cubiccodingtv.persistence.preferences.UserPersistedData
import kotlinx.android.synthetic.main.timeline_fragment.*

class TimelineFragment: Fragment() {

    companion object {
        const val TAG = "TimelineFragment"
        fun newInstance(isSlideshow: Boolean = false, slideTimeUp: Long = 0L): TimelineFragment {
            val fragment = TimelineFragment()
            val args = Bundle()
            args.putBoolean(IS_SLIDESHOW, isSlideshow)
            args.putLong(SLIDE_TIME_UP, slideTimeUp)
            fragment.arguments = args
            return fragment
        }
    }

    private var model: TimelineViewModel? = null
    private val adapter = TimelineAdapter()
    private var smoothScroller: SmoothScroller? = null
    private var slideshowTimer: SlideshowTimer? = null//Only if required will be initialized...

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.timeline_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupViews()
        view?.keepScreenOn = true

        setupSlideshowIfNecessary()
    }

    private fun setupSlideshowIfNecessary() {
        if (arguments?.getBoolean(IS_SLIDESHOW) == true) {
            slideshowTimer = SlideshowTimer(this).apply { startTimeUp(arguments?.getLong(SlideshowActivity.SLIDE_TIME_UP) ?: 0L) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (slideshowTimer?.isRunning() == true) activity?.finish()//If the content was interrupted close all...
        slideshowTimer?.release() ?: Pubsub.INSTANCE.publish(Pubsub.PubsubData(PubsubEvents.CLOSED_TV_CONTENT, null))
    }

    private fun setupViews() {

        //Init the scroller
        smoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }

        currentLevelLabel.text = "Progreso grupo: ${UserPersistedData.classroomName}"

        //Setup recyclerview...
        timelineRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        timelineRecyclerView.adapter = adapter

        val model: TimelineViewModel by viewModels()
        this.model = model
        model.getTimeline().observe(viewLifecycleOwner, Observer {  timelineInfo ->
            handleTimelineObserver(timelineInfo)
        })
    }

    private fun handleTimelineObserver(timelineInfo: TimelineRepository.TimelineInfo?) {
        if (timelineInfo != null) {
            activity?.runOnUiThread {
                val timeline = timelineInfo.timeline
                val currentProgress = timelineInfo.currentProgress
                adapter.setTimelineData(timeline, currentProgress)

                //Recyclerview updates logic...
                timelineRecyclerView.apply {
                    val tmpSmoothScroller = smoothScroller
                    if (tmpSmoothScroller != null) {
                        tmpSmoothScroller.targetPosition = (currentProgress).coerceAtMost(timeline.size - 1)
                        (layoutManager as? LinearLayoutManager)?.startSmoothScroll(tmpSmoothScroller)
                    } else {
                        layoutManager?.scrollToPosition(currentProgress)
                    }
                }

                //Progress updates logic...
                progressBar.max = timeline.size
                progressBar.progress = timeline.size.coerceAtMost(currentProgress)
                experienceSteps.visibility = View.VISIBLE
                experienceSteps.text = getString(R.string.levels_format, (currentProgress + 1).toString(), timeline.size.toString())
            }
        } else {
            errorMessage.visibility = View.VISIBLE
        }
    }
}
