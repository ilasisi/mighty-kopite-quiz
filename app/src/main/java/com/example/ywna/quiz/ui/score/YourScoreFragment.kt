package com.example.ywna.quiz.ui.score

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ywna.quiz.R
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.collections.HashMap

class YourScoreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_your_score, container, false)
        val txtTotalScore: TextView = root.findViewById(R.id.total_score)
        val progress: ProgressBar = root.findViewById(R.id.progress_circular)
        val progressReset: ProgressBar = root.findViewById(R.id.progress_circular_reset)
        val btnReset: Button = root.findViewById(R.id.reset_question)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val userId = sharedPref?.getString(getString(R.string.user_id), "")

        val database = FirebaseDatabase.getInstance().reference
        val userDB = userId?.let { database.child("users").child(it) }

        val totalScoreListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val totalScore = dataSnapshot.value.toString()
                txtTotalScore.text = totalScore
                if (totalScore.isNotEmpty()) {
                    progress.visibility = View.GONE
                    txtTotalScore.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        val userTypeListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userType = dataSnapshot.value.toString()
                if (userType != "admin") {
                    btnReset.visibility = View.GONE
                } else {
                    btnReset.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        userDB?.child("totalScore")?.addListenerForSingleValueEvent(totalScoreListener)
        userDB?.child("userType")?.addListenerForSingleValueEvent(userTypeListener)

        btnReset.setOnClickListener {
            progressReset.visibility = View.VISIBLE
            btnReset.visibility = View.GONE
            val usersDB = database.child("users")
            usersDB.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for(i in dataSnapshot.children) {
                            val userTypeData = HashMap<String, Boolean>()
                            val weeklyScoreData = HashMap<String, Int>()
                            userTypeData["attemptQuestion"] = false
                            weeklyScoreData["weeklyScore"] = 0
                            i.key?.let { userId ->
                                usersDB.child(userId).updateChildren(weeklyScoreData as Map<String, Any>)
                                usersDB.child(userId).updateChildren(userTypeData as Map<String, Any>)
                            }
                        }
                        progressReset.visibility = View.GONE
                        btnReset.visibility = View.VISIBLE
                        Toast.makeText(
                            requireContext(),
                            "Reset successfully",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        progressReset.visibility = View.GONE
                        btnReset.visibility = View.VISIBLE
                        Toast.makeText(
                            requireContext(),
                            "Reset was not successful",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.w(TAG, "updateQuiz:onCancelled", databaseError.toException())
                    }
                }
            )
        }
        return root
    }

    companion object {
        private const val TAG = "Result Fragment"
    }
}