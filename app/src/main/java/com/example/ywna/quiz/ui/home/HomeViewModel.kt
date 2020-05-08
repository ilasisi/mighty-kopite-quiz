package com.example.ywna.quiz.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeViewModel : ViewModel() {

    var name: MutableLiveData<String> = MutableLiveData()
    var attemptQuestion: MutableLiveData<Boolean> = MutableLiveData()
    var attemptPrediction: MutableLiveData<Boolean> = MutableLiveData()

    fun getName(userId: String): LiveData<String> {
        FirebaseDatabase.getInstance().reference
            .child("users").child(userId).child("name")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val userName = p0.value.toString()
                    name.postValue(userName)
                }
            })
        return name
    }

    fun getAttemptQuestion(userId: String): LiveData<Boolean> {
        FirebaseDatabase.getInstance().reference
            .child("users").child(userId).child("attemptQuestion")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val userAttemptQuestion = p0.value.toString().toBoolean()
                    attemptQuestion.postValue(userAttemptQuestion)
                }
            })
        return attemptQuestion
    }

    fun getAttemptPrediction(userId: String): LiveData<Boolean> {
        FirebaseDatabase.getInstance().reference
            .child("users").child(userId).child("attemptPrediction")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val userAttemptPrediction = p0.value.toString().toBoolean()
                    attemptPrediction.postValue(userAttemptPrediction)
                }
            })
        return attemptPrediction
    }
}