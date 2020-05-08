package com.example.ywna.quiz.ui.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class QuizViewModel : ViewModel() {
    val questionTime: MutableLiveData<Long> = MutableLiveData()
    val questionArray: MutableLiveData<ArrayList<ArrayList<String>>> = MutableLiveData()
    val questionArrayTmp = ArrayList<ArrayList<String>>()

    fun getQuestionTime(): LiveData<Long> {
        FirebaseDatabase.getInstance().reference
            .child("questionBank").child("time")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    val time = p0.value.toString().toLong()
                    questionTime.postValue(time)
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })
        return questionTime
    }

    fun getQuestions(): LiveData<ArrayList<ArrayList<String>>> {
        FirebaseDatabase.getInstance().reference
            .child("questionBank").child("questions")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    for (i in p0.children) {
                        val question: ArrayList<String> = i.value as ArrayList<String>
                        questionArrayTmp.add(question)
                        questionArray.postValue(questionArrayTmp)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        return questionArray
    }
}