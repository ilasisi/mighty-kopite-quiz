package com.example.ywna.quiz.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.ywna.quiz.R
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private var viewModel: HomeViewModel = HomeViewModel()
    private var txtName: TextView? = null
    private var btnStartQuiz: Button? = null
//    private var btnPredictGames: Button? = null
    private var ivLogout: ImageView? = null
    private var progress: ProgressBar? = null
    private var startQuizContainer: LinearLayout? = null
//    private var startPredictionContainer: LinearLayout? = null
    private var attemptQuestion: Boolean? = false
//    private var attemptPrediction: Boolean? = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val userId = sharedPref?.getString(getString(R.string.user_id), "")

        initView(root)
        getUserName(userId!!)
        getUserAttemptQuestion(userId)
//        getUserAttemptPrediction(userId)

        btnStartQuiz?.setOnClickListener { view ->
            if (attemptQuestion!!) {
                Toast.makeText(
                    requireActivity(), getString(R.string.already_attempt_question),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                view.findNavController().navigate(R.id.navigation_quiz)
            }
        }

//        btnPredictGames?.setOnClickListener { view ->
//            if (attemptPrediction!!) {
//                Toast.makeText(
//                    requireActivity(), getString(R.string.already_attempt_prediction),
//                    Toast.LENGTH_LONG
//                ).show()
//            } else {
//                view.findNavController().navigate(R.id.navigation_prediction)
//            }
//        }

        ivLogout?.setOnClickListener {
            logout()
        }
        return root
    }

    private fun initView(root: View) {
        txtName = root.findViewById(R.id.txt_name)
        btnStartQuiz = root.findViewById(R.id.start_quiz)
//        btnPredictGames = root.findViewById(R.id.start_prediction)
        ivLogout = root.findViewById(R.id.iv_logout)
        progress = root.findViewById(R.id.progress_circular)
        startQuizContainer = root.findViewById(R.id.start_quiz_container)
//        startPredictionContainer = root.findViewById(R.id.start_prediction_container)
    }

    private fun getUserName(userId: String) {
        viewModel.getName(userId).observe(requireActivity(), Observer { userName ->
            if (userName.isNotEmpty()) {
                txtName?.text = userName
                progress?.visibility = View.GONE
//                startPredictionContainer?.visibility = View.VISIBLE
                startQuizContainer?.visibility = View.VISIBLE
            }
        })
    }

    private fun getUserAttemptQuestion(userId: String) {
        viewModel.getAttemptQuestion(userId).observe(requireActivity(), Observer { userAttemptQuestion ->
            attemptQuestion = userAttemptQuestion
            if (attemptQuestion!!) {
                btnStartQuiz?.background =
                    context?.let { ContextCompat.getDrawable(it, R.drawable.option_button_bg) }
            } else {
                btnStartQuiz?.background =
                    context?.let { ContextCompat.getDrawable(it, R.drawable.wrong_option_bg) }
            }
        })
    }

//    private fun getUserAttemptPrediction(userId: String) {
//        viewModel.getAttemptPrediction(userId).observe(requireActivity(), Observer { userAttemptPrediction ->
//            attemptPrediction = userAttemptPrediction
//            if (attemptPrediction!!) {
//                btnPredictGames?.background =
//                    context?.let { ContextCompat.getDrawable(it, R.drawable.option_button_bg) }
//            } else {
//                btnPredictGames?.background =
//                    context?.let { ContextCompat.getDrawable(it, R.drawable.wrong_option_bg) }
//            }
//        })
//    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        view?.findNavController()?.navigate(R.id.navigation_login)
    }
}