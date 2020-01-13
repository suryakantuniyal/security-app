package com.example.myapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.controller.PreferenceHelper

class WelcomeActivity : AppCompatActivity() {
    private var username_wc :TextView?=null
    private var location_wc : TextView?=null
    private var preferenceHelper : PreferenceHelper?=null

    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        preferenceHelper = PreferenceHelper(this)

        username_wc = findViewById(R.id.userwelcome_tv)
        location_wc = findViewById(R.id.location_tv)

        val signout = findViewById(R.id.logout_btn) as Button

        username_wc!!.text = "Welcome "+ preferenceHelper!!.getNames()
        location_wc!!.setText("Your Location is " +preferenceHelper!!.getLocation())


        signout?.setOnClickListener{
            preferenceHelper!!.putIsLogin(false)
            val intent = Intent(this@WelcomeActivity, Registration::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_out)
            animation.start()
            startActivity(intent)
            this@WelcomeActivity.finish()
        }
    }
}
