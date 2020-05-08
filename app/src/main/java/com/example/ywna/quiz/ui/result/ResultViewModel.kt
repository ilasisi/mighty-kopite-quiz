package com.example.ywna.quiz.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ResultViewModel : ViewModel() {

    private val attemptQuestionNo = MutableLiveData<Int>()
    private val totalScore = MutableLiveData<Int>()

    fun getAttemptQuestionNoValue(userId: String): LiveData<Int> {
        FirebaseDatabase.getInstance().reference.child("users")
            .child(userId).child("attemptQuestionNo")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val attemptQuestionNoValue = dataSnapshot.value.toString().toInt()
                    attemptQuestionNo.postValue(attemptQuestionNoValue)
                }
            })

        return attemptQuestionNo
    }

    fun getTotalScore(userId: String): LiveData<Int> {
        FirebaseDatabase.getInstance().reference.child("users")
            .child(userId).child("totalScore")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val totalScoreValue = dataSnapshot.value.toString().toInt()
                    totalScore.postValue(totalScoreValue)
                }
            })

        return totalScore
    }
}