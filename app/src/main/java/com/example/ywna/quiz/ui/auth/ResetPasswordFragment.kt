package com.example.ywna.quiz.ui.auth

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController

import com.example.ywna.quiz.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ResetPasswordFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var loadingContainer: FrameLayout? = null
    private var resetPasswordFormContainer: LinearLayout? = null
    private var etEmail: EditText? = null
    private var btnResetPassword: Button? = null
    private var tvRegister: TextView?  = null
    private var tvLogin: TextView?  = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_forgot_password, container, false)

        auth = FirebaseAuth.getInstance()

        loadingContainer = root.findViewById(R.id.fl_progress_layout)
        resetPasswordFormContainer = root.findViewById(R.id.email_login_form)
        etEmail = root.findViewById(R.id.login_email)
        btnResetPassword = root.findViewById(R.id.bt_reset_password)

        btnResetPassword?.setOnClickListener {
            val email = etEmail?.text.toString().trim()
            resetPassword(email)
        }
        tvRegister = root.findViewById(R.id.tv_new_user)
        tvRegister?.setOnClickListener {
            view?.findNavController()?.navigate(R.id.navigation_register)
        }

        tvLogin = root.findViewById(R.id.tv_login)
        tvLogin?.setOnClickListener {
            view?.findNavController()?.navigate(R.id.navigation_login)
        }
        return root
    }

    override fun onStart() {
        super.onStart()
        val user: FirebaseUser? = null
        if (user != null) {
            view?.findNavController()?.navigate(R.id.navigation_home)
        }
    }

    private fun resetPassword(email: String) {
        Log.d(TAG, "resetPassword: $email")

        if (!validateForm()) {
            return
        }

        loadingContainer?.visibility = View.VISIBLE
        resetPasswordFormContainer?.visibility = View.GONE

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    view?.findNavController()?.navigate(R.id.navigation_login)
                    Toast.makeText(requireContext(), "Successful! Please check your email to reset password.",
                        Toast.LENGTH_LONG).show()
                    loadingContainer?.visibility = View.GONE
                    resetPasswordFormContainer?.visibility = View.VISIBLE
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "resetPassword:failure", task.exception)
                    Toast.makeText(requireContext(), "Error! unable to send reset password link.",
                        Toast.LENGTH_LONG).show()
                    loadingContainer?.visibility = View.GONE
                    resetPasswordFormContainer?.visibility = View.VISIBLE
                }
            }
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = etEmail?.text.toString()

        if (TextUtils.isEmpty(email)) {
            etEmail?.error = "Email is required"
            valid = false
        } else {
            etEmail?.error = null
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail?.error = "Invalid Email"
        }

        return valid
    }
    companion object {
        private const val TAG = "Login Fragment"
    }
}
