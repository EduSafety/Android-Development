package com.dicoding.edusafety.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dicoding.edusafety.R

import com.dicoding.edusafety.databinding.ActivityMainBinding
import com.dicoding.edusafety.viewmodel.MainViewModel
import com.dicoding.edusafety.viewmodel.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var navigationView: BottomNavigationView
    private lateinit var binding : ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        navigationView = findViewById(R.id.bottomNavigationView)
        navigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment())
                }

                R.id.leaderboard -> {
                    replaceFragment(LeaderboardFragment())
                }

                R.id.profile -> {
                    replaceFragment(ProfileFragment())
                }

                R.id.consult -> {
                    replaceFragment(ConsultationFragment())
                }


            }
            true
        }
        binding.fab.setOnClickListener{
            startActivity(Intent(this,InitialReport::class.java))
        }
        auth = Firebase.auth
//        validate()
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

    private fun validate(){
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