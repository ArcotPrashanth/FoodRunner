package com.internshala.foodrunner.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.internshala.foodrunner.R

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        Handler().postDelayed({
            val startAct = Intent(this@WelcomeActivity, LoginActivity::class.java)
            startActivity(startAct)
            finish()
        }, 2000)
    }
}