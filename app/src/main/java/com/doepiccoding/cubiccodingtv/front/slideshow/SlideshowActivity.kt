package com.doepiccoding.cubiccodingtv.front.slideshow

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.doepiccoding.cubiccodingtv.R
import com.doepiccoding.cubiccodingtv.front.home.score.ScoreboardFragment
import com.doepiccoding.cubiccodingtv.front.home.timeline.TimelineFragment
import com.doepiccoding.cubiccodingtv.front.slideshow.video.VideoFragment
import com.doepiccoding.cubiccodingtv.front.utils.isActivityAlive
import com.doepiccoding.cubiccodingtv.front.utils.showFancyToast
import com.doepiccoding.cubiccodingtv.model.networking.calls.MediaContentRequest
import com.doepiccoding.cubiccodingtv.model.pubsub.Pubsub
import com.doepiccoding.cubiccodingtv.model.pubsub.PubsubEvents
import com.doepiccoding.cubiccodingtv.model.utils.Constants.CUBICCODING_MX_VIDEO_RESOURCES_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class SlideshowActivity: AppCompatActivity(), Pubsub.Listener {

    companion object {
        const val IS_SLIDESHOW = "is.slidehow.key"
        const val SLIDE_TIME_UP = "slide.time.up.key"
        const val DELAY_BETWEEN_SLIDES = 750L
        const val SLIDE_TIME_SCOREBOARD = 120000L
        const val SLIDE_TIME_TIMELINE = 30000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.slideshow_activity)

        Pubsub.INSTANCE.addListener(this, PubsubEvents.FETCH_NEXT_CONTENT_SLIDESHOW)

        //Get the media resources and then start slide
        lifecycleScope.launch(Dispatchers.Main) {
            listOfMediaContent = withContext(Dispatchers.IO) {
                try {
                    MediaContentRequest.getMediaResources()
                } catch (e: Exception) {
                    Timber.e("Error while getting media resources...")
                    listOf<String>()
                }
            }
            if (listOfMediaContent.isEmpty()) {
                showFancyToast(this@SlideshowActivity, getString(R.string.no_media_content))
            }
            handleNextSlideToShow()
        }
    }

    var slideHandler:Handler? = Handler()
    var slideNavigationRunner: Runnable? = null
    private fun navigateToFragment(fragment: Fragment, tag: String) {

        //Always add a delay when launching the next slide to give user's the chance to close this activity...
        slideNavigationRunner = Runnable {
            //Do the fragment change...
            try {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment, tag)
                    .addToBackStack(null).commit()
            } catch (e: Exception) {
                Timber.e("Error when navigating to fragment")
            }
        }
        slideHandler?.postDelayed(slideNavigationRunner, DELAY_BETWEEN_SLIDES)
    }

    override fun onDestroy() {
        super.onDestroy()
        Pubsub.INSTANCE.publish(Pubsub.PubsubData(PubsubEvents.CLOSED_TV_CONTENT, null))
        slideNavigationRunner?.apply { slideHandler?.removeCallbacks(this) }

        slideHandler = null
        slideNavigationRunner = null

        Pubsub.INSTANCE.removeListener(PubsubEvents.FETCH_NEXT_CONTENT_SLIDESHOW, this)
    }

    enum class Slides {
        SCOREBOARD,
        TIMELINE,
        NEXT_MEDIA
    }
    private var currentSlidePosition = 0
    private var currentMediaPosition = 0
    private val slidesTypesOrder = listOf(Slides.SCOREBOARD, Slides.TIMELINE, Slides.NEXT_MEDIA)
    private var listOfMediaContent = listOf<String>()

    private fun handleNextSlideToShow() {
        val slideTypeIndex = currentSlidePosition % slidesTypesOrder.size//round robbin from 0 to numer of slides types
        currentSlidePosition++//Shift to the next type...
        when(slidesTypesOrder[slideTypeIndex]) {
            Slides.SCOREBOARD -> navigateToFragment(ScoreboardFragment.newInstance(true, SLIDE_TIME_SCOREBOARD), ScoreboardFragment.TAG)
            Slides.TIMELINE -> navigateToFragment(TimelineFragment.newInstance(true, SLIDE_TIME_TIMELINE), TimelineFragment.TAG)
            Slides.NEXT_MEDIA -> {
                if (listOfMediaContent.isNotEmpty()) {
                    val mediaIndex = currentMediaPosition % listOfMediaContent.size
                    //TODO: If necessary we can check the media type and decide whether if we need to launch VideoFragment or ImageFragment(not implemented yet...)
                    navigateToFragment(VideoFragment.newInstance(CUBICCODING_MX_VIDEO_RESOURCES_URL + listOfMediaContent[mediaIndex]), VideoFragment.TAG)
                    currentMediaPosition++
                } else {
                    Timber.e("Track, jump to the next type since we have no media...")
                    handleNextSlideToShow()
                }
            }
        }
    }

    override fun onEventReceived(data: Pubsub.PubsubData?) {
        when(data?.eventType) {
            PubsubEvents.FETCH_NEXT_CONTENT_SLIDESHOW -> {
                runOnUiThread {
                    if (isActivityAlive(this@SlideshowActivity)) {
                        handleNextSlideToShow()
                    }
                }
            }
        }
    }
}
