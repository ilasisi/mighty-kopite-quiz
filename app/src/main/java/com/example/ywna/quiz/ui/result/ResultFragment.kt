package com.example.ywna.quiz.ui.result

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.ywna.quiz.R
import com.google.firebase.database.*

class ResultFragment : Fragment() {

    private var viewModel: ResultViewModel = ResultViewModel()
    private var database: DatabaseReference? = null
    private var userDB: DatabaseReference? = null
    private var attemptQuestionNoValue: DatabaseReference? = null
    private var attemptQuestionValue: DatabaseReference? = null
    private var totalScoreValue: DatabaseReference? = null
    private var weeklyScoreValue: DatabaseReference? = null
    private var score: TextView? = null
    private var scoreNote: TextView? = null
    private  var backButton: Button? = null
    private var totalQuestion: Int? = 0
    private var correctAnswer: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            totalQuestion = arguments?.getInt(TOTAL_QUESTION)
            correctAnswer = arguments?.getInt(CORRECT_ANSWER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_result, container, false)

        initView(root)
        setScore()

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val userId = sharedPref?.getString(getString(R.string.user_id), "")

        setDatabaseReference(userId!!)
        setTotalScore(userId)
        setAttemptQuestionNo(userId)
        setUserValue()

        backButton?.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_home)
        }

        return root
    }

    private fun initView(root: View) {
        score = root.findViewById(R.id.score)
        scoreNote = root.findViewById(R.id.score_note)
        backButton = root.findViewById(R.id.back_button)
    }

    private fun setDatabaseReference(userId: String) {
        database = FirebaseDatabase.getInstance().reference
        userDB = userId.let { database?.child("users")?.child(it) }
        totalScoreValue = userDB?.child("totalScore")
        weeklyScoreValue = userDB?.child("weeklyScore")
        attemptQuestionValue = userDB?.child("attemptQuestion")
        attemptQuestionNoValue = userDB?.child("attemptQuestionNo")
    }

    private fun setScore() {
        score?.text = correctAnswer.toString()
        scoreNote?.text = getString(R.string.score_note, correctAnswer, totalQuestion)
        if (correctAnswer != null) {
            if (correctAnswer!! < 5) {
                context?.let {
                    ContextCompat.getColor(
                        it,
                        R.color.colorPrimary
                    )
                }?.let { score?.setTextColor(it) }
            }
        }
    }

    private fun setAttemptQuestionNo(userId: String) {
        viewModel.getAttemptQuestionNoValue(userId).observe(requireActivity(), Observer { attemptQuestionNo ->
            attemptQuestionNoValue?.setValue(attemptQuestionNo + 1)
        })
    }

    private fun setTotalScore(userId: String) {
        viewModel.getTotalScore(userId).observe(requireActivity(), Observer { totalScore ->
            totalScoreValue?.setValue(totalScore + correctAnswer!!)
        })
    }

    private fun setUserValue() {
        weeklyScoreValue?.setValue(correctAnswer)
        attemptQuestionValue?.setValue(true)
    }

    companion object {
        const val TOTAL_QUESTION = "total_question"
        const val CORRECT_ANSWER = "correct_answer"
    }
}
