package com.example.ywna.quiz.ui.leaderboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ywna.quiz.model.User
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class LeaderBoardViewModel : ViewModel() {
    var userList: MutableLiveData<ArrayList<User>> = MutableLiveData()

    fun getAllUser(listUser: ArrayList<User>): LiveData<ArrayList<User>> {
        FirebaseDatabase.getInstance().reference
            .child("users")
            .orderByChild("totalScore").addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {}

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    val key = p0.key
                    if (key != null) {
                        val user = p0.getValue<User>(User::class.java)!!
                        listUser.forEach {
                            if (it.key == key) {
                                it.copy(user)
                                return
                            }
                        }
                    }
                    userList.postValue(listUser)
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    val user = p0.getValue<User>(User::class.java)
                    if (user != null) {
                        listUser.add(user)
                    }
                    userList.postValue(listUser)
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    val person = p0.getValue<User>(User::class.java)
                    if (person != null) {
                        listUser.remove(person)
                    }
                    userList.postValue(listUser)
                }

            })

        return userList
    }

    fun getAllUserWeeklyScore(listUser: ArrayList<User>): LiveData<ArrayList<User>> {
        FirebaseDatabase.getInstance().reference
            .child("users")
            .orderByChild("weeklyScore").addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {}

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    val key = p0.key
                    if (key != null) {
                        val user = p0.getValue<User>(User::class.java)!!
                        listUser.forEach {
                            if (it.key == key) {
                                it.copy(user)
                                return
                            }
                        }
                    }
                    userList.postValue(listUser)
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    val user = p0.getValue<User>(User::class.java)
                    if (user != null) {
                        listUser.add(user)
                    }
                    userList.postValue(listUser)
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    val person = p0.getValue<User>(User::class.java)
                    if (person != null) {
                        listUser.remove(person)
                    }
                    userList.postValue(listUser)
                }

            })

        return userList
    }
}