package com.doepiccoding.cubiccodingtv.front.home.score

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.doepiccoding.cubiccodingtv.R

class ScoreboardFragment: Fragment() {

    companion object {
        const val TAG = "ScoreboardFragment"
        fun newInstance(): ScoreboardFragment {
            val fragment = ScoreboardFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    var runnerHandler: Handler? = Handler()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        runnerHandler?.postDelayed(stopperRunnable, 5000)
        return inflater.inflate(R.layout.scoreboard_fragment, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        runnerHandler?.removeCallbacks(stopperRunnable)
        runnerHandler = null
    }

    private val stopperRunnable = Runnable {
        parentFragmentManager.popBackStack()
        //TODO: Pubsub notify is done...
    }
}
