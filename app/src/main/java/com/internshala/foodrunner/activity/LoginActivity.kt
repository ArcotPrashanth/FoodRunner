package com.internshala.foodrunner.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.foodrunner.R
import com.internshala.foodrunner.adapter.DashboardRecyclerAdapter
import com.internshala.foodrunner.model.Rest
import com.internshala.foodrunner.util.ConnectionManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.json.JSONObject
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    lateinit var etMobileNumber: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button
    lateinit var txtForgotPassword: TextView
    lateinit var txtRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etMobileNumber = findViewById(R.id.etMobileNumber)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtForgotPassword = findViewById(R.id.txtForgotPassword)
        txtRegister = findViewById(R.id.txtRegister)

        btnLogin.setOnClickListener {
            val queue = Volley.newRequestQueue(this@LoginActivity)
            val url = "http://13.235.250.119/v2/login/fetch_result/"

            val jsonParams = JSONObject()
            jsonParams.put("mobile_number", etMobileNumber.text.toString())
            jsonParams.put("password", etPassword.text.toString())

            if (ConnectionManager().checkConnectivity(this@LoginActivity)) {
                val intent =
                    Intent(this@LoginActivity, DashboardActivity::class.java)
                val jsonRequest =
                    object : JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        jsonParams,
                        Response.Listener { response ->
                            try {
                                val data = response.getJSONObject("data")
                                val success = data.getBoolean("success")
                                if (success) {

                                    startActivity(intent)
                                } else {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Incorrect Credentials",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        },
                        Response.ErrorListener { error: VolleyError? ->
                            Toast.makeText(this@LoginActivity, error?.message, Toast.LENGTH_SHORT)
                                .show()
                        }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "7e39d01900538f"
                            return headers
                        }
                    }

                queue.add(jsonRequest)
            } else {
                val dialog = AlertDialog.Builder(this@LoginActivity)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection is not Found")
                dialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    finish()
                }

                dialog.setNegativeButton("Exit") { text, listener ->
                    ActivityCompat.finishAffinity(this@LoginActivity)
                }
                dialog.create()
                dialog.show()
            }
        }

        txtForgotPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPassActivity::class.java))
        }
        txtRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
