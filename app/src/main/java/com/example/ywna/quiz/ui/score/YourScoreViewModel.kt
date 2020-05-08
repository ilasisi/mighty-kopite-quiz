package com.example.ywna.quiz.ui.score

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class YourScoreViewModel: ViewModel() {
    var predictionResultArray: MutableLiveData<ArrayList<ArrayList<String>>> = MutableLiveData()
    var userAnswerArray: MutableLiveData<ArrayList<ArrayList<String>>> = MutableLiveData()
    var userType: MutableLiveData<String> = MutableLiveData()
    var totalScore: MutableLiveData<String> = MutableLiveData()

    fun getPredictionResult(): LiveData<ArrayList<ArrayList<String>>> {
        if (predictionResultArray.value == null) {
            FirebaseDatabase.getInstance()
                .reference.child("predictionResult")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val predictionResult = p0.value as ArrayList<ArrayList<String>>
                        predictionResultArray.postValue(predictionResult)
                    }
                })
        }
        return predictionResultArray
    }

    fun getUserAnswer(userId: String): LiveData<ArrayList<ArrayList<String>>> {
        if (userAnswerArray.value == null) {
            FirebaseDatabase.getInstance()
                .reference.child("predictions").child(userId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val userAnswer = p0.value as ArrayList<ArrayList<String>>
                        userAnswerArray.postValue(userAnswer)
                    }
                })
        }
        return userAnswerArray
    }

    fun getUserType(userId: String): LiveData<String> {
        FirebaseDatabase.getInstance()
            .reference.child("users").child(userId).child("userType")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userTypeValue = dataSnapshot.value.toString()
                    userType.postValue(userTypeValue)

            }
        })
        return userType
    }

    fun getTotalScore(userId: String): LiveData<String> {
        FirebaseDatabase.getInstance()
            .reference.child("users").child(userId).child("totalScore")
            .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val totalScoreValue = dataSnapshot.value.toString()
                totalScore.postValue(totalScoreValue)
            }
        })
        return totalScore
    }

    fun setUsersData(context: Context, progressReset: ProgressBar, btnResetQuiz: Button) {
        val usersDB = FirebaseDatabase.getInstance().reference.child("users")
        usersDB.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (i in dataSnapshot.children) {
                        val attemptQuestionData = HashMap<String, Boolean>()
                        val weeklyScoreData = HashMap<String, Int>()
                        attemptQuestionData["attemptQuestion"] = false
                        weeklyScoreData["weeklyScore"] = 0
                        i.key?.let { userId ->
                            usersDB.child(userId)
                                .updateChildren(weeklyScoreData as Map<String, Any>)
                            usersDB.child(userId)
                                .updateChildren(attemptQuestionData as Map<String, Any>)
                        }
                    }
                    progressReset.visibility = View.GONE
                    btnResetQuiz.visibility = View.VISIBLE
                    Toast.makeText(
                        context,
                        "Reset successfully",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    progressReset.visibility = View.GONE
                    btnResetQuiz.visibility = View.VISIBLE
                    Toast.makeText(
                        context,
                        "Reset was not successful",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }
}