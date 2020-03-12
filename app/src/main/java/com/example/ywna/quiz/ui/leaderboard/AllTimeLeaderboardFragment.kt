package com.example.ywna.quiz.ui.leaderboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.example.ywna.quiz.R
import com.example.ywna.quiz.model.User
import com.google.firebase.database.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ywna.quiz.adapter.AllTimeLeaderboardAdapter
import java.util.*
import kotlin.collections.ArrayList


class AllTimeLeaderboardFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var progress: ProgressBar
    private lateinit var recyclerView: RecyclerView

    private lateinit var allTimeLeaderboardAdapter: AllTimeLeaderboardAdapter
    private val listUser = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_all_time_leaderboard, container, false)

        recyclerView = root.findViewById(R.id.rc_leaderboard)
        progress = root.findViewById(R.id.progress_circular)

        databaseReference = FirebaseDatabase.getInstance().reference.child("users")

        addSingleEventListener()
        addChildEventListener()
        return root
    }

    private  fun setListViewAdapter() {
        recyclerView.itemAnimator = DefaultItemAnimator()
        val mLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = mLayoutManager
        Collections.sort(listUser, User.BY_TOTAL_SCORE)
        listUser.reverse()
        allTimeLeaderboardAdapter = AllTimeLeaderboardAdapter(requireActivity(), listUser)
        recyclerView.adapter = allTimeLeaderboardAdapter
    }

    private fun addSingleEventListener() {
        databaseReference.orderByChild("totalScore").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                progress.visibility = View.GONE
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
    private fun addChildEventListener() {
        databaseReference.orderByChild("totalScore").addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val key = p0.key
                if(key != null){
                    val user = p0.getValue<User>(User::class.java)!!
                    listUser.forEach {
                        if(it.key == key){
                            it.copy(user)
                            allTimeLeaderboardAdapter.notifyDataSetChanged()
                            return
                        }
                    }
                }
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val user = p0.getValue<User>(User::class.java)
                if(user != null){
                    listUser.add(user)
                    setListViewAdapter()
                    allTimeLeaderboardAdapter.notifyDataSetChanged()
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val person = p0.getValue<User>(User::class.java)
                if(person != null){
                    listUser.remove(person)
                    allTimeLeaderboardAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    companion object {
        private const val TAG = "All Time Leaderboard Fragment"
    }
}