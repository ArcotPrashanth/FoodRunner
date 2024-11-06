package com.internshala.foodrunner.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.internshala.foodrunner.R
import com.internshala.foodrunner.adapter.DashboardRecyclerAdapter
import com.internshala.foodrunner.model.Rest
import com.internshala.foodrunner.util.ConnectionManager
import org.json.JSONObject
import java.lang.Exception

class RestDetailsFragment : Fragment() {

    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerDashboard: RecyclerView
    lateinit var recyclerAdapter: DashboardRecyclerAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    val restInfoList = arrayListOf<Rest>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE
        layoutManager = LinearLayoutManager(activity)

        id
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/" + id

        if (ConnectionManager().checkConnectivity(activity as Context)) {

            val jsonObjectRequest =
                object :
                    JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        null,
                        Response.Listener<JSONObject> { response ->

                            try {
                                progressLayout.visibility = View.GONE
                                val data = response.getJSONObject("data")
                                val success = data.getBoolean("success")
                                if (success) {
                                    val restArray = data.getJSONArray("data")
                                    for (i in 0 until restArray.length()) {
                                        val restJsonObject = restArray.getJSONObject(i)
                                        val restObject = Rest(
                                            restJsonObject.getString("id").toInt(),
                                            restJsonObject.getString("name"),
                                            restJsonObject.getString("rating"),
                                            restJsonObject.getString("cost_for_one").toInt(),
                                            restJsonObject.getString("image_url")
                                        )
                                        restInfoList.add(restObject)
                                        if (activity != null) {
                                            recyclerAdapter =
                                                DashboardRecyclerAdapter(
                                                    restInfoList,
                                                    activity as Context
                                                )
                                            val mLayoutManager = LinearLayoutManager(activity)

                                            recyclerDashboard.layoutManager = mLayoutManager
                                            recyclerDashboard.itemAnimator = DefaultItemAnimator()
                                            recyclerDashboard.adapter = recyclerAdapter
                                            recyclerDashboard.setHasFixedSize(true)
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        },
                        Response.ErrorListener { error: VolleyError? ->
                            Toast.makeText(activity as Context, error?.message, Toast.LENGTH_SHORT)
                                .show()

                        }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "7e39d01900538f"
                        return headers
                    }
                }
            queue.add(jsonObjectRequest)
        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("No Internet Connection found. Please connect to the internet and re-open the app.")
            dialog.setCancelable(false)
            dialog.setPositiveButton("Ok") { _, _ ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }
        return view
    }

}
