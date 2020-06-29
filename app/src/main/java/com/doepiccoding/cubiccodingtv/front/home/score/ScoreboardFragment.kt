package com.doepiccoding.cubiccodingtv.front.home.score

import android.os.Bundle
import android.os.Handler
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
import com.doepiccoding.cubiccodingtv.front.utils.isFragmentAlive
import com.doepiccoding.cubiccodingtv.model.networking.calls.ScoreboardRequest
import com.doepiccoding.cubiccodingtv.persistence.preferences.UserPersistedData
import kotlinx.android.synthetic.main.scoreboard_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class ScoreboardFragment : Fragment() {

    companion object {
        const val TAG = "ScoreboardFragment"
        fun newInstance(): ScoreboardFragment {
            val fragment = ScoreboardFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    private val adapter by lazy { ScoreboardAdapter() }
    private var bouncingScroll: BouncingScroll? = null

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

        fetchScoreboard()
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

    private fun fetchScoreboard() {
        lifecycleScope.launch(Dispatchers.IO) {
            val scoreboardDataResponse = try {
                ScoreboardRequest.getScore(UserPersistedData.classroomName)
            } catch (e: Exception) {
                Timber.e(e, "Error while getting the scoreboard...")
                null
            }

            lifecycleScope.launch(Dispatchers.Main) {
                if (isFragmentAlive(this@ScoreboardFragment)) {
                    if (scoreboardDataResponse != null) {
                        handleScoresListChanged(
                            scoreboardDataResponse.tournamentName,
                            scoreboardDataResponse.score
                        )
                    } else {
                        emptyScoreText.text = getString(R.string.error_loading_scores)
                        emptyScoreText.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun handleScoresListChanged(
        tournnament: String,
        scoreboardItems: List<ScoreboardDataItem>
    ) {
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
    }

    private fun startBouncingLogic(totalItems: Int, layoutManager: LinearLayoutManager) {
        bouncingScroll = BouncingScroll(context, totalItems, layoutManager)
        bouncingScroll?.start()
    }

}
