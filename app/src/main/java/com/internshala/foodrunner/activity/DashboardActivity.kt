package com.internshala.foodrunner.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.internshala.foodrunner.*
import com.internshala.foodrunner.fragment.*
import kotlinx.android.synthetic.main.activity_main.*

class DashboardActivity : AppCompatActivity() {

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var previousMenuItem: MenuItem? = null

        var drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        toolbar = findViewById(R.id.toolbar)
        var navigationView: NavigationView = findViewById(R.id.navigationView)
        setUpToolbar()

        setupActionBarToggle()

        openDashboard()

        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = true
            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            val mPendingRunnable = Runnable { drawerLayout.closeDrawer(GravityCompat.START) }
            Handler().postDelayed(mPendingRunnable, 100)

            when (it.itemId) {
                R.id.menuDashboard -> {
                    openDashboard()
                    drawerLayout.closeDrawers()
                }
                R.id.menuFav -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            FavouritesFragment()
                        )
                        .commit()
                    supportActionBar?.title = "Favourite Restaurants"
                    drawerLayout.closeDrawers()
                }
                R.id.menuProfile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            ProfileFragment()
                        )
                        .commit()

                    supportActionBar?.title = "My Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.menuHistory -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            HistoryFragment()
                        )
                        .commit()

                    supportActionBar?.title = "Order History"
                    drawerLayout.closeDrawers()
                }
                R.id.menuFaq -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            FaqFragment()
                        )
                        .commit()

                    supportActionBar?.title = "FAQs"
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupActionBarToggle() {
        actionBarDrawerToggle =
            object : ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.openDrawer,
                R.string.closeDrawer
            ) {
                override fun onDrawerStateChanged(newState: Int) {
                    super.onDrawerStateChanged(newState)
                    val pendingRunnable = Runnable {
                        val inputMethodManager =
                            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                    }
                    Handler().postDelayed(pendingRunnable, 50)
                }
            }

        drawerLayout.addDrawerListener(actionBarDrawerToggle)

        actionBarDrawerToggle.syncState()

    }

    fun openDashboard() {
        val fragment = DashboardFragment()
        val transaction = supportFragmentManager?.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
        supportActionBar?.title = "All Restaurants"
        navigationView.setCheckedItem(R.id.menuDashboard)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)

        when (frag) {
            !is DashboardFragment -> openDashboard()
            else -> super.onBackPressed()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item?.itemId == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
}

