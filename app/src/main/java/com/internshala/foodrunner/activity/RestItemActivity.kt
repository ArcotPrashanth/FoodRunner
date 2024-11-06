package com.internshala.foodrunner.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RestItemActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(this@RestItemActivity,"This is RestItemActivity",Toast.LENGTH_SHORT).show()
    }

}