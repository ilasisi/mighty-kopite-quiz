package com.example.ywna.quiz.ui.auth

import android.content.Context
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
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var loadingContainer: FrameLayout? = null
    private var loginFormContainer: LinearLayout? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var btnLogin: Button? = null
    private var tvRegister: TextView?  = null
    private var tvForgotPassword: TextView?  = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_login, container, false)

        auth = FirebaseAuth.getInstance()

        loadingContainer = root.findViewById(R.id.fl_progress_layout)
        loginFormContainer = root.findViewById(R.id.email_login_form)
        etEmail = root.findViewById(R.id.login_email)
        etPassword = root.findViewById(R.id.login_password)
        btnLogin = root.findViewById(R.id.bt_login)

        btnLogin?.setOnClickListener {
            val email = etEmail?.text.toString().trim()
            val password = etPassword?.text.toString().trim()
            loginUser(email, password)
        }
        tvRegister = root.findViewById(R.id.tv_new_user)
        tvRegister?.setOnClickListener {
            view?.findNavController()?.navigate(R.id.navigation_register)
        }
        tvForgotPassword = root.findViewById(R.id.tv_forgot_password)
        tvForgotPassword?.setOnClickListener {
            view?.findNavController()?.navigate(R.id.navigation_reset_password)
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

    private fun loginUser(email: String, password: String) {
        Log.d(TAG, "signIn: $email")

        if (!validateForm()) {
            return
        }

        loadingContainer?.visibility = View.VISIBLE
        loginFormContainer?.visibility = View.GONE

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser

                    val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
                    with (sharedPref.edit()) {
                        putString(getString(R.string.user_id), user?.uid)
                        commit()
                    }
                    view?.findNavController()?.navigate(R.id.navigation_home)
                    loadingContainer?.visibility = View.GONE
                    loginFormContainer?.visibility = View.VISIBLE
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(requireContext(), "Authentication failed.",
                        Toast.LENGTH_LONG).show()
                    loadingContainer?.visibility = View.GONE
                    loginFormContainer?.visibility = View.VISIBLE
                }
            }
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = etEmail?.text.toString()
        val password = etPassword?.text.toString()

        if (TextUtils.isEmpty(email)) {
            etEmail?.error = "Email is required"
            valid = false
        } else {
            etEmail?.error = null
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail?.error = "Invalid Email"
        }

        if (TextUtils.isEmpty(password)) {
            etPassword?.error = "Password is required"
            valid = false
        } else {
            etPassword?.error = null
        }

        return valid
    }
    companion object {
        private const val TAG = "Login Fragment"
    }
}
