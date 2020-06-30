package com.doepiccoding.cubiccodingtv.front.home

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.doepiccoding.cubiccodingtv.R
import com.doepiccoding.cubiccodingtv.front.home.score.ScoreboardFragment
import com.doepiccoding.cubiccodingtv.front.utils.setFocusFrameListeners
import com.doepiccoding.cubiccodingtv.persistence.preferences.UserPersistedData
import kotlinx.android.synthetic.main.home_activity.*
import timber.log.Timber

class Home: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.e("Track, Created!")
        setContentView(R.layout.home_activity)

        setupViews()

        //TODO: Remove test code after implementing video player fragment...
//        videoView.setVideoPath("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4")
//        videoView.setOnErrorListener{ mp, what, extra ->
//            Timber.e("Track, mp: $mp, what: $what, extra: $extra")
//            true
//        }
//        videoView.start()
    }

    private fun setupViews() {

        setFocusFrameListeners(listOf(scoreboardCard, progressCard, slideShowCard))

        scoreboardCard.requestFocus()
        scoreboardCard.setOnClickListener {
            cardsGroup.visibility = View.GONE
            navigateToFragment(ScoreboardFragment.newInstance(), ScoreboardFragment.TAG)
            Timber.e("Track, Scoreboard card clicked")
        }

        groupNameIndicator.text = getString(R.string.group_value, UserPersistedData.classroomName)
    }

    private fun navigateToFragment(fragment: Fragment, tag: String) {
        //Do the fragment change...
        try {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment, tag)
                .addToBackStack(null)
                .commit()
        } catch (e: Exception) {
            Timber.e("Error when navigating to fragment")
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        return super.dispatchKeyEvent(event)
        //TODO: Fire pubsub with key event for those screens that might need it...
    }
}
