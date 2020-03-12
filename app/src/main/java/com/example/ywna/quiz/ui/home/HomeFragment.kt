package com.example.ywna.quiz.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.ywna.quiz.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private var txtName: TextView? = null
    private var attemptQuestion: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val btnStartQuiz: Button = root.findViewById(R.id.start_quiz)
        val ivLogout: ImageView = root.findViewById(R.id.iv_logout)
        val progress: ProgressBar = root.findViewById(R.id.progress_circular)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val userId = sharedPref?.getString(getString(R.string.user_id), "")
        txtName = root.findViewById(R.id.txt_name)

        val database = FirebaseDatabase.getInstance().reference
        val userDB = userId?.let { database.child("users").child(it) }
        val nameValue = userDB?.child("name")
        val attemptQuestionValue = userDB?.child("attemptQuestion")

        val nameListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val name = dataSnapshot.value.toString()
                if (name.isNotEmpty()) {
                    txtName?.text = name
                    progress.visibility = View.GONE
                    btnStartQuiz.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        val attemptQuestionListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                attemptQuestion = dataSnapshot.value.toString().toBoolean()

                if (attemptQuestion!!) {
                    btnStartQuiz.background = context?.let { ContextCompat.getDrawable(it, R.drawable.option_button_bg) }
                } else {
                    btnStartQuiz.background = context?.let { ContextCompat.getDrawable(it, R.drawable.wrong_option_bg) }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "attemptQuestion:onCancelled", databaseError.toException())
            }
        }

        attemptQuestionValue?.addListenerForSingleValueEvent(attemptQuestionListener)
        nameValue?.addListenerForSingleValueEvent(nameListener)

        btnStartQuiz.setOnClickListener { view ->
            if (attemptQuestion!!) {
                Toast.makeText(requireActivity(), getString(R.string.already_attempt_question),
                    Toast.LENGTH_LONG).show()
            } else {
                view.findNavController().navigate(R.id.navigation_quiz)
            }
        }
        ivLogout.setOnClickListener {
            logout()
        }
        return root
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        view?.findNavController()?.navigate(R.id.navigation_login)
    }

    companion object {
        private const val TAG = "Home Fragment"
    }
}