package com.doepiccoding.cubiccodingtv.front.slideshow.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.doepiccoding.cubiccodingtv.R
import com.doepiccoding.cubiccodingtv.front.home.Home
import com.doepiccoding.cubiccodingtv.front.slideshow.SlideshowActivity
import com.doepiccoding.cubiccodingtv.front.utils.isFragmentAlive
import com.doepiccoding.cubiccodingtv.front.utils.showFancyToast
import com.doepiccoding.cubiccodingtv.model.networking.calls.MediaContentRequest
import com.doepiccoding.cubiccodingtv.model.pubsub.Pubsub
import com.doepiccoding.cubiccodingtv.model.pubsub.PubsubEvents
import com.doepiccoding.cubiccodingtv.model.utils.Constants.CUBICCODING_MX_VIDEO_RESOURCES_URL
import kotlinx.android.synthetic.main.video_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class VideoFragment : Fragment() {

    companion object {
        const val TAG = "VideoFragment"
        const val VIDEO_PATH = "video.path.key"
        private const val ALL_VIDEOS = "all.videos.key"

        fun newInstance(videoUrl: String?): VideoFragment {
            val fragment = VideoFragment()
            val args = Bundle()
            if (videoUrl == null) {
                args.putBoolean(ALL_VIDEOS, true)
            } else {
                args.putString(VIDEO_PATH, videoUrl)
                args.putBoolean(ALL_VIDEOS, false)

            }
            fragment.arguments = args

            return fragment
        }
    }

    private var listOfMediaContent = listOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.video_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (arguments?.getBoolean(ALL_VIDEOS) == true) {
            startAllVideos();
        } else {
            startVideo()
        }
        view?.keepScreenOn = true
    }

    override fun onStop() {
        super.onStop()
        if(isRemoving) {
            if (videoView?.isPlaying == true) {
                //Only kill host activity if is the slide show, any other activity should reamin untouched...
                (activity as? SlideshowActivity)?.finish()

                try {
                    Timber.e("Track, stop playback...")
                    videoView?.stopPlayback()
                }catch (e: Exception) {
                    Timber.e(e, "Error while stopping playback...")
                }
            }

            if (activity is Home) {//Bring the icons back for home activity...
                Pubsub.INSTANCE.publish(Pubsub.PubsubData(PubsubEvents.CLOSED_TV_CONTENT, null))


            }
        }
    }

    private fun startAllVideos() {
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
                showFancyToast(context, getString(R.string.no_media_content))
            }
            if (isFragmentAlive(this@VideoFragment)) {
                configurePlayAllVideoCallbacks()
                startPlayingNextInAllVideos()
            }
        }
    }

    var currentVideoIndex = 0;
    private fun startPlayingNextInAllVideos() {
        val videoIndex = currentVideoIndex % listOfMediaContent.size
        videoView.setVideoPath(CUBICCODING_MX_VIDEO_RESOURCES_URL + listOfMediaContent[videoIndex])
        videoView.start()
    }

    private fun configurePlayAllVideoCallbacks() {
        videoView.setOnErrorListener { mp, what, extra ->
            Timber.e("Track, mp: $mp, what: $what, extra: $extra")
            true
        }
        videoView.setOnCompletionListener {
            currentVideoIndex++
            startPlayingNextInAllVideos()
        }
    }

    private fun startVideo() {
        videoView.setVideoPath(arguments?.getString(VIDEO_PATH))
        videoView.setOnErrorListener { mp, what, extra ->
            Timber.e("Track, mp: $mp, what: $what, extra: $extra")
            true
        }
        videoView.setOnCompletionListener {
            activity?.supportFragmentManager?.popBackStack()
            Pubsub.INSTANCE.publish(Pubsub.PubsubData(PubsubEvents.FETCH_NEXT_CONTENT_SLIDESHOW, null))
        }
        videoView.start()
    }

}
