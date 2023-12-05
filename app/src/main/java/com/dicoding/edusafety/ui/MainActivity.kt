package com.dicoding.edusafety.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dicoding.edusafety.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var navigationView: BottomNavigationView

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
        //simulasi login, delete soon (kalo fitur login dah bisa eakk)
//        isLogin()
//        Log.d("MainActivity","isLogin: $isLogin")
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().apply {
            replace(R.id.navHost, fragment)
            commit()
        }
    }

//    //simulasi login, delete soon
//    private fun isLogin(){
//        if (!isLogin){
//            startActivity(Intent(this,InitialPage::class.java))
//        }
//    }

    //delete soon
//    companion object{
//        var isLogin = false
//    }

}