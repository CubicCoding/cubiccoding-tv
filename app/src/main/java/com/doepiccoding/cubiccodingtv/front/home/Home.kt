package com.doepiccoding.cubiccodingtv.front.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.doepiccoding.cubiccodingtv.R
import com.doepiccoding.cubiccodingtv.front.home.score.ScoreboardFragment
import com.doepiccoding.cubiccodingtv.front.utils.setFocusFrameListeners
import kotlinx.android.synthetic.main.home_activity.*
import timber.log.Timber

class Home: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.e("Track, Created!")
        setContentView(R.layout.home_activity)

        setupViews()
    }

    private fun setupViews() {

        setFocusFrameListeners(listOf(scoreboardCard, progressCard, lightCard, slideShowCard))

        scoreboardCard.requestFocus()
        scoreboardCard.setOnClickListener {
            cardsGroup.visibility = View.GONE
            navigateToFragment(ScoreboardFragment.newInstance(), ScoreboardFragment.TAG)
            Timber.e("Track, Scoreboard card clicked")
        }
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
}