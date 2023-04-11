package com.example.foodiezone.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.foodiezone.Fragment.*
import com.example.foodiezone.R
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var frameLayout: FrameLayout
    private lateinit var navigationView: NavigationView
    private lateinit var coordinatorLayout: CoordinatorLayout

    private var previousMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        title="Home Activity"
        openDashboard()

        if (intent != null) {

            /*Fetching the details from the intent*/
            val details = intent.getBundleExtra("details")

            /*Getting the value of data from the bundle object*/
            val data = details?.getString("data")

            /*Checking the location from which data was sent*/
            if (data == "login") {
                /*Creating the text to be displayed*/
                val text = "Mobile Number : ${details?.getString("mobile")} \n " +
                        "Password : ${details?.getString("password")}"
//                txtData.text = text
                Toast.makeText(this,"$text",Toast.LENGTH_LONG).show()
            }


            if (data == "register") {
                val text = " Name : ${details?.getString("name")} \n " +
                        "Mobile Number : ${details?.getString("mobile")} \n " +
                        "Password : ${details?.getString("password")} \n " +
                        "Address: ${details?.getString("address")}"
                Toast.makeText(this,"$text",Toast.LENGTH_LONG).show()
            }

            if (data == "forgot") {
                val text = "Mobile Number : ${details?.getString("mobile")} \n " +
                        "Email : ${details?.getString("email")}"
                Toast.makeText(this,"$text",Toast.LENGTH_LONG).show()
            }

        } else {
            /*No data was received through the intent*/
            Toast.makeText(this,"No Data Received!!!",Toast.LENGTH_LONG).show()
        }


        drawerLayout = findViewById(R.id.drawerLayout)
        frameLayout = findViewById(R.id.frameLayout)
        navigationView = findViewById(R.id.navigationView)
        coordinatorLayout = findViewById(R.id.coordinateLayout)
        toolbar = findViewById(R.id.toolbar)
        navigationView.setCheckedItem(R.id.home)
        setUpToolbar()
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@HomeActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false

            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it
            when (it.itemId) {
                R.id.home -> {
                    openDashboard()
                    navigationView.setCheckedItem(R.id.home)
                    drawerLayout.closeDrawers()
                }
                R.id.favRestaurant -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, FavouritesFragment())
                        .addToBackStack("Favourite Restaurants")
                        .commit()
                    supportActionBar?.title = "Favourite Restaurants"
                    drawerLayout.closeDrawers()
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, ProfileFragment())
                        .addToBackStack("Profile")
                        .commit()
                    supportActionBar?.title = "Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.faq -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, FaqFragment())
                        .addToBackStack("FAQ")
                        .commit()
                    supportActionBar?.title = "FAQ"
                    drawerLayout.closeDrawers()
                }
                R.id.logout -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, LogoutFragment())
                        .addToBackStack("Logout")
                        .commit()
                    supportActionBar?.title = "Logout"
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true

        }


    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openDashboard() {

        val fragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
        supportActionBar?.title = "Home"

    }

    override fun onBackPressed() {

        when (supportFragmentManager.findFragmentById(R.id.frameLayout)) {
            !is HomeFragment -> openDashboard()

            else -> return onBackPressed()
        }
    }
}