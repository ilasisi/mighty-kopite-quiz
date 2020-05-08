package com.example.ywna.quiz.ui.prediction

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.ywna.quiz.R
import com.google.firebase.database.*
import org.jetbrains.anko.okButton
import org.jetbrains.anko.support.v4.alert
import kotlin.collections.ArrayList


class PredictionFragment : Fragment() {

    private var viewModel: PredictionViewModel = PredictionViewModel()
    private var textHome: TextView? = null
    private var textAway: TextView? = null

    private var optionAway: Button? = null
    private var optionDraw: Button? = null
    private var optionHome: Button? = null
    private var btnNext: Button? = null

    private var predictionLayout: RelativeLayout? = null
    private var progress: ProgressBar? = null

    private var selectedAnswer = ""

    private lateinit var databaseReference: DatabaseReference
    private lateinit var attemptPredictionValue: DatabaseReference
    private var predictionArray = ArrayList<ArrayList<String>>()
    private var resultArray = ArrayList<ArrayList<String>>()

    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_prediction, container, false)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        userId = sharedPref?.getString(getString(R.string.user_id), "")

        databaseReference = FirebaseDatabase.getInstance().reference
        attemptPredictionValue = databaseReference.child("users").child(userId!!).child("attemptPrediction")

        initView(root)
        loadPredictions()
        return root
    }

    private fun initView(root: View) {
        textHome = root.findViewById(R.id.home_team)
        textAway = root.findViewById(R.id.away_team)
        optionAway = root.findViewById(R.id.btn_away)
        optionHome = root.findViewById(R.id.btn_home)
        optionDraw = root.findViewById(R.id.btn_draw)
        btnNext = root.findViewById(R.id.btn_next)
        predictionLayout = root.findViewById(R.id.prediction_layout)
        progress = root.findViewById(R.id.progress_circular)
    }

    private fun loadPredictions() {
        viewModel.getPredictions().observe(requireActivity(), Observer { predictions ->
            predictionArray = predictions
            progress?.visibility = View.GONE
            predictionLayout?.visibility = View.VISIBLE
            showNextPrediction()
        })
    }

    private fun showNextPrediction() {
        if (predictionArray.size > 0) {
            setButtonOnclick()
            setupDefaultButton()
            val prediction = predictionArray[0]

            textHome?.text = prediction[0]
            textAway?.text = prediction[1]
            prediction.removeAt(0)
            predictionArray.removeAt(0)
        } else {
            alert("You have attempted all prediction for this week") {
                okButton {
                    userId?.let { it1 -> databaseReference.child("predictions").child(it1).setValue(resultArray) }
                    attemptPredictionValue.setValue(true)
                    view?.findNavController()?.navigate(R.id.navigation_home)
                }
                isCancelable = false
            }.show()
        }
    }

    private fun setButtonOnclick() {

        optionAway?.setOnClickListener {
            selectedAnswer = "Away"
            optionAway?.background =
                context?.let { ContextCompat.getDrawable(it, R.drawable.selected_option_bg) }
            optionHome?.background =
                context?.let { ContextCompat.getDrawable(it, R.drawable.option_button_bg) }
            optionDraw?.background =
                context?.let { ContextCompat.getDrawable(it, R.drawable.option_button_bg) }
        }

        optionHome?.setOnClickListener {
            selectedAnswer = "Home"
            optionHome?.background =
                context?.let { ContextCompat.getDrawable(it, R.drawable.selected_option_bg) }
            optionAway?.background =
                context?.let { ContextCompat.getDrawable(it, R.drawable.option_button_bg) }
            optionDraw?.background =
                context?.let { ContextCompat.getDrawable(it, R.drawable.option_button_bg) }
        }

        optionDraw?.setOnClickListener {
            selectedAnswer = "Draw"
            optionDraw?.background =
                context?.let { ContextCompat.getDrawable(it, R.drawable.selected_option_bg) }
            optionHome?.background =
                context?.let { ContextCompat.getDrawable(it, R.drawable.option_button_bg) }
            optionAway?.background =
                context?.let { ContextCompat.getDrawable(it, R.drawable.option_button_bg) }
        }

        btnNext?.setOnClickListener {
            showNextPrediction()
            val tmpArray = ArrayList<String>()
            val homeTeam = textHome?.text.toString()
            val awayTeam = textAway?.text.toString()
            tmpArray.add("$awayTeam vs $homeTeam")
            tmpArray.add(selectedAnswer)
            resultArray.addAll(listOf(tmpArray))
        }
    }

    private fun setupDefaultButton() {
        optionHome?.background =
            context?.let { ContextCompat.getDrawable(it, R.drawable.option_button_bg) }
        optionAway?.background =
            context?.let { ContextCompat.getDrawable(it, R.drawable.option_button_bg) }
        optionDraw?.background =
            context?.let { ContextCompat.getDrawable(it, R.drawable.option_button_bg) }
    }
}