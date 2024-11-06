package com.internshala.foodrunner.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.internshala.foodrunner.R

class ForgotPassActivity : AppCompatActivity() {

    lateinit var etForgotMobile: EditText
    lateinit var etForgotEmail: EditText
    lateinit var btnForgotNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
    }
}