package com.example.ywna.quiz.ui.leaderboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.example.ywna.quiz.R
import com.example.ywna.quiz.model.User
import com.google.firebase.database.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ywna.quiz.adapter.WeeklyLeaderboardAdapter
import java.util.*
import kotlin.collections.ArrayList


class WeeklyLeaderboardFragment : Fragment() {

    private var viewModel: LeaderBoardViewModel = LeaderBoardViewModel()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var progress: ProgressBar
    private lateinit var recyclerView: RecyclerView

    private lateinit var weeklyLeaderboardAdapter: WeeklyLeaderboardAdapter
    private var listUser = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_weekly_leaderboard, container, false)

        recyclerView = root.findViewById(R.id.rc_weekly_leaderboard)
        progress = root.findViewById(R.id.progress_circular)

        databaseReference = FirebaseDatabase.getInstance().reference.child("users")

        loadAllUserScores()
        return root
    }

    private  fun setListViewAdapter() {
        recyclerView.itemAnimator = DefaultItemAnimator()
        val mLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = mLayoutManager
        Collections.sort(listUser, User.BY_WEEKLY_SCORE)
        listUser.reverse()
        weeklyLeaderboardAdapter = WeeklyLeaderboardAdapter(requireActivity(), listUser)
        recyclerView.adapter = weeklyLeaderboardAdapter
    }

    private fun loadAllUserScores() {
        viewModel.getAllUserWeeklyScore(listUser).observe(requireActivity(), Observer { userList ->
            listUser = userList
            setListViewAdapter()
            progress.visibility = View.GONE
        })
    }
}