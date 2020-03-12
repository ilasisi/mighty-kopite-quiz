package com.example.ywna.quiz.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ywna.quiz.R
import com.example.ywna.quiz.model.User
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat

class AllTimeLeaderboardAdapter(private val activity: Activity, private val listUser: List<User>) : RecyclerView.Adapter<AllTimeLeaderboardAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(activity)
                .inflate(R.layout.leaderboard_content, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listUser[position]

        holder.textViewName?.text = user.name
        holder.textViewTotalScore?.text = user.totalScore.toString()
        holder.textViewSerialNo?.text = (position + 1).toString()
        holder.textViewQuizCount?.text = user.attemptQuestionNo.toString()
        if (position < 3) {
            holder.textViewSerialNoContainer?.background = ContextCompat.getDrawable(activity, android.R.color.holo_green_dark)
        } else {
            holder.textViewSerialNoContainer?.background = ContextCompat.getDrawable(activity, android.R.color.holo_red_dark)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewName: TextView? = itemView.findViewById(R.id.txt_name)
        var textViewTotalScore: TextView? = itemView.findViewById(R.id.total_score)
        var textViewQuizCount: TextView? = itemView.findViewById(R.id.quiz_count)
        var textViewSerialNo: TextView? = itemView.findViewById(R.id.serial_no)
        var textViewSerialNoContainer: RelativeLayout? = itemView.findViewById(R.id.serial_no_container)
    }
}

class WeeklyLeaderboardAdapter(private val activity: Activity, private val listUser: List<User>) : RecyclerView.Adapter<WeeklyLeaderboardAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(activity)
                .inflate(R.layout.leaderboard_content, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listUser[position]

        holder.textViewName?.text = user.name
        holder.textViewWeeklyScore?.text = user.weeklyScore.toString()
        holder.textViewSerialNo?.text = (position + 1).toString()
        if (position < 3) {
            holder.textViewSerialNoContainer?.background = ContextCompat.getDrawable(activity, android.R.color.holo_green_dark)
        } else {
            holder.textViewSerialNoContainer?.background = ContextCompat.getDrawable(activity, android.R.color.holo_red_dark)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewName: TextView? = itemView.findViewById(R.id.txt_name)
        var textViewWeeklyScore: TextView? = itemView.findViewById(R.id.total_score)
        var textViewSerialNo: TextView? = itemView.findViewById(R.id.serial_no)
        var textViewSerialNoContainer: RelativeLayout? = itemView.findViewById(R.id.serial_no_container)
    }
}