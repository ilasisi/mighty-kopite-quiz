package com.example.ywna.quiz.ui.score

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.ywna.quiz.R
import com.google.firebase.database.*


class YourScoreFragment : Fragment() {

    private var viewModel: YourScoreViewModel = YourScoreViewModel()
    private var database: DatabaseReference? = null
    private var userDB: DatabaseReference? = null
    private var txtTotalScore: TextView? = null
    private var progress: ProgressBar? = null
    private var progressReset: ProgressBar? = null
    private var progressInitResult: ProgressBar? = null
    private var btnResetQuiz: Button? = null
    private var adminLayout: LinearLayout? = null
//    private var btnInitPredictionResult: Button? = null
//    private var userAnswerArray = ArrayList<ArrayList<String>>()
//    private var resultArray = ArrayList<ArrayList<String>>()
//    private var correctPrediction = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_your_score, container, false)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val userId = sharedPref?.getString(getString(R.string.user_id), "")

        userDB = database?.child("users")?.child(userId!!)
        initView(root)
//        loadPredictions(userId!!)
        userId?.let { loadTotalScore(it) }
        userId?.let { loadUserType(it) }

        btnResetQuiz?.setOnClickListener {
            resetUsersData()
        }

//        btnInitPredictionResult?.setOnClickListener {
//            if (resultArray.isNotEmpty()) {
//                compareArray()
//            } else {
//                toast("Result not loaded yet")
//            }
//        }

        return root
    }

    private fun initView(root: View) {
        txtTotalScore = root.findViewById(R.id.total_score)
        progress = root.findViewById(R.id.progress_circular)
        progressReset = root.findViewById(R.id.progress_circular_reset)
        btnResetQuiz = root.findViewById(R.id.reset_question)
        progressInitResult = root.findViewById(R.id.progress_circular_reset)
//        btnInitPredictionResult = root.findViewById(R.id.init_prediction)
        adminLayout = root.findViewById(R.id.admin_buttons)
    }

    private fun loadUserType(userId: String) {
        viewModel.getUserType(userId).observe(requireActivity(), Observer { userType ->
            if (userType != "admin") {
                adminLayout?.visibility = View.GONE
            } else {
                adminLayout?.visibility = View.VISIBLE
            }
        })
    }

    private fun loadTotalScore(userId: String) {
        viewModel.getTotalScore(userId).observe(requireActivity(), Observer { totalScore  ->
            if (totalScore.isNotEmpty()) {
                txtTotalScore?.text = totalScore
                progress?.visibility = View.GONE
                txtTotalScore?.visibility = View.VISIBLE
            }
        })
    }

//    private fun loadPredictions(userId: String) {
//        viewModel.getPredictionResult().observe(requireActivity(), Observer { predictionResult ->
//            resultArray = predictionResult
//        })
//
//        viewModel.getUserAnswer(userId).observe(requireActivity(), Observer { userAnswer ->
//            userAnswerArray = userAnswer
//        })
//    }

    private fun resetUsersData() {
        progressReset?.visibility = View.VISIBLE
        btnResetQuiz?.visibility = View.GONE
        progressReset?.let { btnResetQuiz?.let { it1 ->
            viewModel.setUsersData(requireContext(), it,
                it1
            )
        } }
    }

//    private fun compareArray() {
//        resultArray.forEach { answer ->
//            userAnswerArray.forEach { result ->
//                if (answer[1] == result[1]) {
//                    correctPrediction++
//                }
//            }
//        }
//        Log.d("TESTCOMPAREAN", correctPrediction.toString())
//    }

    companion object {
        private const val TAG = "Result Fragment"
    }
}