package com.example.ywna.quiz.ui.score

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.ywna.quiz.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ResultFragment : Fragment() {

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
        val score: TextView = root.findViewById(R.id.score)
        val scoreNote: TextView = root.findViewById(R.id.score_note)
        val backButton: Button = root.findViewById(R.id.back_button)

        score.text = correctAnswer.toString()
        scoreNote.text = getString(R.string.score_note, correctAnswer, totalQuestion)
        if (correctAnswer != null) {
            if (correctAnswer!! < 5) {
                context?.let {
                    ContextCompat.getColor(
                        it,
                        R.color.colorPrimary
                    )
                }?.let { score.setTextColor(it) }
            }
        }

        backButton.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_home)
        }

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val userId = sharedPref?.getString(getString(R.string.user_id), "")

        val database = FirebaseDatabase.getInstance().reference
        val userDB = userId?.let { database.child("users").child(it) }
        val totalScoreValue = userDB?.child("totalScore")
        val weeklyScoreValue = userDB?.child("weeklyScore")
        val attemptQuestionValue = userDB?.child("attemptQuestion")
        val attemptQuestionNoValue = userDB?.child("attemptQuestionNo")

        val totalScoreListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val totalScore = dataSnapshot.value.toString().toInt()
                totalScoreValue?.setValue(totalScore + correctAnswer!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "totalScore:onCancelled", databaseError.toException())
            }
        }

        val attemptQuestionNoListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val attemptQuestionNo = dataSnapshot.value.toString().toInt()
                attemptQuestionNoValue?.setValue(attemptQuestionNo + 1)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "totalScore:onCancelled", databaseError.toException())
            }
        }

        weeklyScoreValue?.setValue(correctAnswer)
        attemptQuestionValue?.setValue(true)
        totalScoreValue?.addListenerForSingleValueEvent(totalScoreListener)
        attemptQuestionNoValue?.addListenerForSingleValueEvent(attemptQuestionNoListener)

        return root
    }

    companion object {
        const val TOTAL_QUESTION = "total_question"
        const val CORRECT_ANSWER = "correct_answer"
        private const val TAG = "Result Fragment"
    }
}
