package com.doepiccoding.cubiccodingtv.front.slideshow.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.doepiccoding.cubiccodingtv.R
import com.doepiccoding.cubiccodingtv.model.pubsub.Pubsub
import com.doepiccoding.cubiccodingtv.model.pubsub.PubsubEvents
import kotlinx.android.synthetic.main.video_fragment.*
import timber.log.Timber

class VideoFragment : Fragment() {

    companion object {
        const val TAG = "VideoFragment"
        const val VIDEO_PATH = "video.path.key"
        fun newInstance(videoUrl: String): VideoFragment {
            val fragment = VideoFragment()
            val args = Bundle()
            args.putString(VIDEO_PATH, videoUrl)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.video_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        startVideo()
        view?.keepScreenOn = true
    }

    override fun onStop() {
        super.onStop()
        if(isRemoving) {
            if (videoView?.isPlaying == true) {
                activity?.finish()
                try {
                    Timber.e("Track, stop playback...")
                    videoView?.stopPlayback()
                }catch (e: Exception) {
                    Timber.e(e, "Error while stopping playback...")
                }
            }
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
