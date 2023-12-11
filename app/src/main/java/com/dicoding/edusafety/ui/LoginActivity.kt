package com.dicoding.edusafety.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dicoding.edusafety.R
import com.dicoding.edusafety.data.pref.UserModel
import com.dicoding.edusafety.databinding.ActivityLoginBinding
import com.dicoding.edusafety.viewmodel.LoginActivityViewModel
import com.dicoding.edusafety.viewmodel.LoginViewModel
import com.dicoding.edusafety.viewmodel.LoginViewModelFactory
import com.dicoding.edusafety.viewmodel.ViewModelFactory
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

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.loginBtn.setOnClickListener {
            if (areAllFieldsFilled()) {
                // All fields are filled, navigate to MainActivity
            val factoryApi: LoginViewModelFactory = LoginViewModelFactory.getInstance()
            val viewModelApi =
                ViewModelProvider(this, factoryApi)[LoginActivityViewModel::class.java]

            val factoryPref: ViewModelFactory = ViewModelFactory.getInstance(this)
            val viewModel = ViewModelProvider(this, factoryPref)[LoginViewModel::class.java]

            viewModelApi.isLoading.observe(this) {
                showLoading(it)
                Log.d("LOADING", "$it")
            }

            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()


            viewModelApi.login(email, password)
            viewModelApi.validLogin.observe(this) {
                   if (it != null) {
                        validLogin(it)
                        viewModelApi.resetRegisterResponse()
                        viewModelApi.token.observe(this, Observer { token ->
                            viewModel.saveSession(UserModel(email, token.toString(), true))
                            startActivity(Intent(this, MainActivity::class.java))
                            Log.d("TOKEN MASUK", "$token")
                       })
                   }
               }
            } else {
                // Display error messages for empty fields
                checkAndSetErrorForEmptyField(binding.emailEditText, binding.emailContainer, "Email is required")
                checkAndSetErrorForEmptyField(binding.passwordEditText, binding.passwordContainer, "Password is required")
            }
          }
        }

        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Configure Google Sign In
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

        //login validation
        emailFocusListener()
        passwordFocusListener()
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
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun emailFocusListener() {
        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.emailContainer.helperText = validEmail()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun validEmail(): String? {
        val emailText = binding.emailEditText.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return "Invalid Email Address"
        }
        return null
    }

    private fun passwordFocusListener() {
        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.passwordContainer.helperText = validPassword()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun validPassword(): String? {
        val passwordText = binding.passwordEditText.text.toString()
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

    private fun areAllFieldsFilled(): Boolean {
        return binding.passwordEditText.text?.isNotBlank() == true &&
                binding.emailEditText.text?.isNotBlank() == true
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

    companion object {
        private const val TAG = "LoginActivity"
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun validLogin(isValid: Boolean) {
        if (!isValid) {
            Handler(Looper.getMainLooper()).post {
                showAlertDialog("Oh no!", "Email atau Password salah !", "OK")
            }
            showLoading(false)
            Log.d("GAGAL LOGIN", "Fail")
        }
    }

//    private fun showAlertDialogLogin(title: String, message: String, buttonPos: String) {
//        AlertDialog.Builder(this).apply {
//            setTitle(title)
//            setMessage(message)
//            setPositiveButton(buttonPos) { _, _ ->
//                val intent = Intent(context, MainActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                startActivity(intent)
//                finish()
//            }
//            create()
//            show()
//        }
//    }

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
}