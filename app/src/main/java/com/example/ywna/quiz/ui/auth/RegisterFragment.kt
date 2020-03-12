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

import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.example.ywna.quiz.R
import com.example.ywna.quiz.model.User
import com.google.firebase.database.FirebaseDatabase


class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var loadingContainer: FrameLayout? = null
    private var registerFormContainer: LinearLayout? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var etConfirmPassword: EditText? = null
    private var etName: EditText? = null
    private var btnRegister: Button? = null
    private var tvLogin: TextView?  = null
    private var user = User()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_register, container, false)

        auth = FirebaseAuth.getInstance()

        loadingContainer = root.findViewById(R.id.fl_progress_layout)
        registerFormContainer = root.findViewById(R.id.email_register_form)
        etEmail = root.findViewById(R.id.register_email)
        etPassword = root.findViewById(R.id.register_password)
        etConfirmPassword = root.findViewById(R.id.register_confirm_password)
        etName = root.findViewById(R.id.register_name)
        btnRegister = root.findViewById(R.id.bt_register)

        btnRegister?.setOnClickListener{
            val email = etEmail?.text.toString().trim()
            val password = etPassword?.text.toString().trim()
            val name = etName?.text.toString().trim()
            registerUser(email, password, name)
        }

        tvLogin = root.findViewById(R.id.tv_login)
        tvLogin?.setOnClickListener {
            view?.findNavController()?.navigate(R.id.navigation_login)
        }

        return root
    }

    private fun registerUser(email: String, password: String, name: String) {
        Log.d(TAG, "signIn: $email $name")

        if (!validateForm()) {
            return
        }

        loadingContainer?.visibility = View.VISIBLE
        registerFormContainer?.visibility = View.GONE
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()){ task ->
                Log.d(TAG, "Started")
                if (task.isSuccessful) {
                    loadingContainer?.visibility = View.GONE
                    registerFormContainer?.visibility = View.VISIBLE
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(context, "Login Successful, kindly login to continue.",
                        Toast.LENGTH_LONG).show()
                    val userId = auth.currentUser?.uid
                    // Write user details to the database
                    writeNewUser(userId, name, email)
                    view?.findNavController()?.navigate(R.id.navigation_login)
                } else {
                    loadingContainer?.visibility = View.GONE
                    registerFormContainer?.visibility = View.VISIBLE
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun writeNewUser(userId: String?, name: String, email: String) {
        val database = FirebaseDatabase.getInstance().reference
        user.key = database.push().key.toString()
        user.name = name
        user.email = email
        user.totalScore = 0
        user.attemptQuestion = false
        user.userType = "user"
        userId?.let { database.child("users").child(it).setValue(user) }
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = etEmail?.text.toString()
        val password = etPassword?.text.toString()
        val name = etName?.text.toString()

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

        if (etConfirmPassword?.text.toString() != etPassword?.text.toString()) {
            etConfirmPassword?.error = "Password does not match"
            valid = false
        } else {
            etConfirmPassword?.error = null
        }

        if (TextUtils.isEmpty(name)) {
            etName?.error = "Name is required"
            valid = false
        } else {
            etName?.error = null
        }

        return valid
    }

    companion object {
        private const val TAG = "Register Fragment"
    }
}
