package com.doepiccoding.cubiccodingtv.front.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.doepiccoding.cubiccodingtv.R
import com.doepiccoding.cubiccodingtv.front.home.score.ScoreboardFragment
import com.doepiccoding.cubiccodingtv.front.home.timeline.TimelineFragment
import com.doepiccoding.cubiccodingtv.front.slideshow.SlideshowActivity
import com.doepiccoding.cubiccodingtv.front.utils.isActivityAlive
import com.doepiccoding.cubiccodingtv.front.utils.setFocusFrameListeners
import com.doepiccoding.cubiccodingtv.model.pubsub.Pubsub
import com.doepiccoding.cubiccodingtv.model.pubsub.PubsubEvents
import com.doepiccoding.cubiccodingtv.persistence.preferences.UserPersistedData
import kotlinx.android.synthetic.main.home_activity.*
import timber.log.Timber

class Home: AppCompatActivity(), Pubsub.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Pubsub.INSTANCE.addListener(this, PubsubEvents.CLOSED_TV_CONTENT)
        setContentView(R.layout.home_activity)

        setupViews()
    }

    override fun onDestroy() {
        super.onDestroy()

        Pubsub.INSTANCE.removeListener(PubsubEvents.CLOSED_TV_CONTENT, this)
    }

    private fun setupViews() {

        setFocusFrameListeners(listOf(scoreboardCard, progressCard, slideShowCard))

        scoreboardCard.requestFocus()

        scoreboardCard.setOnClickListener {
            cardsGroup.visibility = View.GONE
            navigateToFragment(ScoreboardFragment.newInstance(), ScoreboardFragment.TAG)
        }

        progressCard.setOnClickListener {
            cardsGroup.visibility = View.GONE
            navigateToFragment(TimelineFragment.newInstance(), TimelineFragment.TAG)
        }

        slideShowCard.setOnClickListener {
            cardsGroup.visibility = View.GONE
            startActivity(Intent(this, SlideshowActivity::class.java))
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

    override fun onEventReceived(data: Pubsub.PubsubData?) {
        when(data?.eventType) {
            PubsubEvents.CLOSED_TV_CONTENT -> {
                runOnUiThread {
                    if (isActivityAlive(this@Home)) {
                        cardsGroup.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}
