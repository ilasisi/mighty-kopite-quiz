package com.example.ywna.quiz.ui.prediction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PredictionViewModel : ViewModel() {
    val predictionArray: MutableLiveData<ArrayList<ArrayList<String>>> = MutableLiveData()
    val predictionArrayTmp = ArrayList<ArrayList<String>>()

    fun getPredictions(): LiveData<ArrayList<ArrayList<String>>> {
        FirebaseDatabase.getInstance().reference
            .child("predictionBank").child("predictions")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    for (i in p0.children) {
                        val question: ArrayList<String> = i.value as ArrayList<String>
                        predictionArrayTmp.add(question)
                        predictionArray.postValue(predictionArrayTmp)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        return predictionArray
    }
}