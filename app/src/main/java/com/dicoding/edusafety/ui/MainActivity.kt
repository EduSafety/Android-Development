package com.dicoding.edusafety.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dicoding.edusafety.R
import com.dicoding.edusafety.viewmodel.MainViewModel
import com.dicoding.edusafety.viewmodel.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var navigationView: BottomNavigationView
    private lateinit var auth: FirebaseAuth

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(HomeFragment())

        navigationView = findViewById(R.id.bottomNavigationView)
        navigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment())
                }

                R.id.profile -> {
                    replaceFragment(ProfileFragment())
                }
            }
            true
        }
        auth = Firebase.auth
//        val authGoogle: Boolean = auth.currentUser != null

//        if (!authGoogle){
//            startActivity(Intent(this, InitialPage::class.java))
//            Log.d("noAuthGoogle", "RUN")
//            finish()
//        } else {
//            viewModel.getSession().observe(this) { user ->
//                if (!user.isLogin) {
//                    startActivity(Intent(this, InitialPage::class.java))
//                    finish()
//                }
//            }
//        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().apply {
            replace(R.id.navHost, fragment)
            commit()
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser == null) {
            startActivity(Intent(this@MainActivity, InitialPage::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Log.d("noAuthGoogle", "RUN ERROR")
            viewModel.getSession().observe(this) { user ->
                if (!user.isLogin) {
                    startActivity(Intent(this@MainActivity, InitialPage::class.java))
                    finish()
                }
            }
        } else {
            updateUI(currentUser)
        }
    }
}