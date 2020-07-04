package com.doepiccoding.cubiccodingtv.front.home.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doepiccoding.cubiccodingtv.R
import com.doepiccoding.cubiccodingtv.front.home.score.components.BouncingScroll
import com.doepiccoding.cubiccodingtv.front.home.score.recyclerview.ScoreboardAdapter
import com.doepiccoding.cubiccodingtv.front.home.score.recyclerview.ScoreboardDataItem
import com.doepiccoding.cubiccodingtv.front.slideshow.SlideshowActivity.Companion.IS_SLIDESHOW
import com.doepiccoding.cubiccodingtv.front.slideshow.SlideshowActivity.Companion.SLIDE_TIME_UP
import com.doepiccoding.cubiccodingtv.front.slideshow.component.SlideshowTimer
import com.doepiccoding.cubiccodingtv.front.utils.isFragmentAlive
import com.doepiccoding.cubiccodingtv.model.dtos.ExpirationPayload
import com.doepiccoding.cubiccodingtv.model.dtos.ScoreboardAndExpirationDto
import com.doepiccoding.cubiccodingtv.model.networking.calls.GroupsRequest
import com.doepiccoding.cubiccodingtv.model.networking.calls.ScoreboardRequest
import com.doepiccoding.cubiccodingtv.model.pubsub.Pubsub
import com.doepiccoding.cubiccodingtv.model.pubsub.PubsubEvents
import com.doepiccoding.cubiccodingtv.persistence.preferences.UserPersistedData
import kotlinx.android.synthetic.main.scoreboard_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class ScoreboardFragment : Fragment() {

    companion object {
        const val TAG = "ScoreboardFragment"
        fun newInstance(isSlideshow: Boolean = false, slideTimeUp: Long = 0L): ScoreboardFragment {
            val fragment = ScoreboardFragment()
            val args = Bundle()
            args.putBoolean(IS_SLIDESHOW, isSlideshow)
            args.putLong(SLIDE_TIME_UP, slideTimeUp)
            fragment.arguments = args
            return fragment
        }
    }

    private val adapter by lazy { ScoreboardAdapter() }
    private var bouncingScroll: BouncingScroll? = null
    private var slideshowTimer: SlideshowTimer? = null//Only if required will be initialized...

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.scoreboard_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupViews()

        fetchScoreboardAndExpiration()

        view?.keepScreenOn = true
        setupSlideshowIfNecessary()
    }

    private fun setupSlideshowIfNecessary() {
        if (arguments?.getBoolean(IS_SLIDESHOW) == true) {
            slideshowTimer = SlideshowTimer(this).apply { startTimeUp(arguments?.getLong(SLIDE_TIME_UP) ?: 0L) }
        }
    }

    private fun setupViews() {
        scoreboardRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        scoreboardRecyclerView.adapter = adapter

        scoreboardRecyclerView?.setOnKeyListener { v, keyCode, event ->
            Timber.e("Keyboard Event: $v, $keyCode, $event")
            false
        }
    }

    private fun fetchScoreboardAndExpiration() {
        lifecycleScope.launch(Dispatchers.IO) {
            val scoreboardAndExpirationDataResponse = try {

                val groupName = UserPersistedData.classroomName
                val scores = ScoreboardRequest.getScore(groupName)
                val expirations = GroupsRequest.getExpirationByGroup(groupName)
                linkExpirationDataWithScoreboardData(expirations, scores.score)
                scores
            } catch (e: Exception) {
                Timber.e(e, "Error while getting the scoreboard...")
                null
            }

            lifecycleScope.launch(Dispatchers.Main) {
                if (isFragmentAlive(this@ScoreboardFragment)) {
                    if (scoreboardAndExpirationDataResponse != null) {
                        handleScoresListChanged(scoreboardAndExpirationDataResponse.tournamentName, scoreboardAndExpirationDataResponse.score)
                    } else {
                        progressBar.visibility = View.GONE
                        emptyScoreText.text = getString(R.string.error_loading_scores)
                        emptyScoreText.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun linkExpirationDataWithScoreboardData(expirations: List<ExpirationPayload>, scores: List<ScoreboardDataItem>) {
        //Link all of the student expiration status with their scoreboard data...
        for (expiration in expirations) {
            for (score in scores) {
                val scoreAndExpiration: ScoreboardAndExpirationDto = score.getData()
                if (scoreAndExpiration.scoreboardItemPayload.email == expiration.student.email) {//If we found the student in the scoreboard, link the expiration and scoreboard and break...
                    scoreAndExpiration.expirationPayload = expiration
                    break
                }
            }
        }
    }

    private fun handleScoresListChanged(tournnament: String, scoreboardItems: List<ScoreboardDataItem>) {
        progressBar.visibility = View.GONE
        if (scoreboardItems.isNotEmpty()) {
            adapter.populateScoreboard(scoreboardItems)
            tournament.text = tournnament
            tournament.visibility = View.VISIBLE
            startBouncingLogic(
                adapter?.itemCount ?: 0,
                scoreboardRecyclerView.layoutManager as LinearLayoutManager
            )
        } else {
            emptyScoreText.text = getString(R.string.no_scores)
            emptyScoreText.visibility = View.VISIBLE
            tournament.visibility = View.INVISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bouncingScroll?.release()
        if (slideshowTimer?.isRunning() == true) activity?.finish()//If the content was interrupted close all...
        slideshowTimer?.release() ?: Pubsub.INSTANCE.publish(Pubsub.PubsubData(PubsubEvents.CLOSED_TV_CONTENT, null))
    }

    private fun startBouncingLogic(totalItems: Int, layoutManager: LinearLayoutManager) {
        bouncingScroll = BouncingScroll(context, totalItems, layoutManager)
        bouncingScroll?.start()
    }

}
