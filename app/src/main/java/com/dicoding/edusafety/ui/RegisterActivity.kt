package com.dicoding.edusafety.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dicoding.edusafety.R
import com.dicoding.edusafety.databinding.ActivityRegisterBinding
import com.dicoding.edusafety.viewmodel.LoginViewModelFactory
import com.dicoding.edusafety.viewmodel.RegisterViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val viewModel by viewModels<RegisterViewModel> {
        LoginViewModelFactory.getInstance()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.loginWithGoogle.setOnClickListener {
            signIn()
        }

        binding.btnRegister.setOnClickListener {
            if (areAllFieldsFilled()) {
                val name = binding.edtFullname.text.toString()
                val accessCode = binding.edtUniversityCode.text.toString()
                val email = binding.edtEmail.text.toString()
                val password = binding.edtPassword.text.toString()
                val phone = binding.edtPhoneNumber.text
                // All fields are filled, navigate to LoginActivity
                Log.d("INPUT","$name, $email, $accessCode,$phone, $password")
                viewModel.register(name, email, accessCode, phone, password)
                viewModel.isLoading.observe(this) {
                    showLoading(it)
                }
                viewModel.validRegist.observe(this, Observer {
                    if (it != null) {
                        validRegister(it)
                        viewModel.resetRegisterResponse()
                    }
                })
            } else {
                // Display error messages for empty fields
                checkAndSetErrorForEmptyField(binding.edtFullname, binding.edtFullnameContainer, "Fullname is required")
                checkAndSetErrorForEmptyField(binding.edtEmail, binding.edtEmailContainer, "Email is required")
                checkAndSetErrorForEmptyField(binding.edtPhoneNumber, binding.edtPhoneNumberContainer, "Phone number is required")
                checkAndSetErrorForEmptyField(binding.edtPassword, binding.edtPasswordContainer, "Password is required")
                checkAndSetErrorForEmptyField(binding.edtConfirmPassword, binding.edtConfirmPasswordContainer, "Confirm Password is required")
                checkAndSetErrorForEmptyField(binding.edtUniversityCode, binding.edtUniversityCodeContainer, "University Code is required")
            }
        }

        setupTextChangeListener(binding.edtFullname, binding.edtFullnameContainer, "Fullname is required")
        setupTextChangeListener(binding.edtFullname, binding.edtUniversityCodeContainer, "University Code is required")
        setupTextChangeListener(binding.edtEmail, binding.edtEmailContainer, "Email is required") { text ->
            validEmail(text)
        }
        setupTextChangeListener(binding.edtPhoneNumber, binding.edtPhoneNumberContainer, "Phone number is required") { text ->
            validPhone(text)
        }
        setupTextChangeListener(binding.edtPassword, binding.edtPasswordContainer, "Password is required") { text ->
            validPassword(text)
        }
        setupTextChangeListener(binding.edtConfirmPassword, binding.edtConfirmPasswordContainer, "Confirm Password is required") { text ->
            validateConfirmPassword(text)
        }

    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private var resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val currentUser = auth.currentUser
                    currentUser?.getIdToken(true)?.addOnSuccessListener { result ->
                        val token = result.token
                        Log.d("TOKEN GOOGLE", "$token")
                    }
                    Log.d(TAG, "signInWithCredential:success")
                    val user: FirebaseUser? = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        currentUser?.getIdToken(false)?.addOnSuccessListener { result ->
            val token = result.token
            Log.d("TOKEN GOOGLE", "$token")
        }
        updateUI(currentUser)
    }


    private fun setupTextChangeListener(
        editText: TextInputEditText,
        container: TextInputLayout,
        emptyMessage: String,
        validationFunction: ((String) -> String?)? = null
    ) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s?.toString()?.trim() ?: ""
                container.helperText = if (text.isEmpty()) emptyMessage else validationFunction?.invoke(text) ?: ""
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validEmail(emailText: String): String? {
        return if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            "Invalid Email Address"
        } else {
            null
        }
    }

    private fun validPassword(passwordText: String): String? {
        if (passwordText.length < 8) {
            return "Minimum 8 Character Password"
        }
        if (!passwordText.matches(".*[A-Z].*".toRegex())) {
            return "Must Contain 1 Upper-case Character"
        }
        if (!passwordText.matches(".*[a-z].*".toRegex())) {
            return "Must Contain 1 Lower-case Character"
        }
        if (!passwordText.matches(".*[@#\$%^&+=].*".toRegex())) {
            return "Must Contain 1 Special Character (@#\$%^&+=)"
        }
        return null
    }

    private fun validPhone(phoneText: String): String? {
        if (!phoneText.matches(".*[0-9].*".toRegex())) {
            return "Must be all Digits"
        }
        if (phoneText.length <= 11) {
            return "Must be 11 Digits"
        }
        return null
    }

    private fun validateConfirmPassword(confirmPasswordText: String): String? {
        val passwordText = binding.edtPassword.text?.toString() ?: ""
        return if (passwordText != confirmPasswordText) {
            "Password doesn't match"
        } else {
            null
        }
    }

    private fun areAllFieldsFilled(): Boolean {
        return binding.edtFullname.text?.isNotBlank() == true &&
                binding.edtEmail.text?.isNotBlank() == true &&
                binding.edtPhoneNumber.text?.isNotBlank() == true &&
                binding.edtPassword.text?.isNotBlank() == true &&
                binding.edtConfirmPassword.text?.isNotBlank() == true &&
                binding.edtUniversityCode.text?.isNotBlank() == true
    }

    private fun checkAndSetErrorForEmptyField(
        editText: TextInputEditText,
        container: TextInputLayout,
        errorMessage: String
    ) {
        if (editText.text?.isBlank() == true) {
            container.helperText = errorMessage
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun validRegister(isValid: Boolean) {
        val email = binding.edtEmail.text.toString()
        if (isValid) {
            Handler(Looper.getMainLooper()).post {
                startActivity(Intent(this, RegisterSuccessActivity::class.java))
                finish()
            }

        } else {
            Handler(Looper.getMainLooper()).post {
                showAlertDialog(
                    "Oh no!",
                    "Akun dengan $email sudah dipakai orang lain nih. Yuk, buat yang berbeda sedikit",
                    "BUAT ULANG"
                )
            }
            Log.d("AKUN GAGAL DIBUAT", "Registrasi Gagal")
        }
    }


    private fun showAlertDialog(title: String, message: String, buttonPos: String) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(buttonPos) { _, _ ->
            }
            create()
            show()
        }
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }
}
