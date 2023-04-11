package com.example.foodiezone.Activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodiezone.R
import com.example.foodiezone.util.ConnectionManager
import com.example.foodiezone.util.LOGIN
import com.example.foodiezone.util.SessionManager
import com.example.foodiezone.util.Validations
import org.json.JSONException
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    lateinit var etMobile: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button
    lateinit var txtForgot: TextView
    lateinit var txtSignup: TextView

    lateinit var sessionManager: SessionManager
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        etMobile = findViewById(R.id.etMobile)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtForgot = findViewById(R.id.txtForgot)
        txtSignup = findViewById(R.id.txtSignup)

        sessionManager= SessionManager(this)
        sharedPreferences=
            this.getSharedPreferences(sessionManager.PREF_NAME,sessionManager.PRIVATE_MODE)

        txtSignup.setOnClickListener(){
            startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
        }
        txtForgot.setOnClickListener(){
            startActivity(Intent(this@LoginActivity, ForgotActivity::class.java))
        }


        btnLogin.setOnClickListener() {
            btnLogin.visibility = View.INVISIBLE

            if(Validations.validateMobile(etMobile.text.toString()) && Validations.validatePasswordLength(etPassword.text.toString())){
                if(ConnectionManager().isNetworkAvailable(this@LoginActivity)){
                    val queue=Volley.newRequestQueue(this@LoginActivity)

                    val jsonParams=JSONObject()
                    jsonParams.put("mobile_number",etMobile.text.toString())
                    jsonParams.put("password",etPassword.text.toString())

                    val jsonObjectRequest = object : JsonObjectRequest(Method.POST, LOGIN, jsonParams,
                        Response.Listener {

                            try {
                                val data = it.getJSONObject("data")
                                val success = data.getBoolean("success")
                                if (success) {
                                    val response = data.getJSONObject("data")
                                    sharedPreferences.edit()
                                        .putString("user_id", response.getString("user_id")).apply()
                                    sharedPreferences.edit()
                                        .putString("user_name", response.getString("name")).apply()
                                    sharedPreferences.edit()
                                        .putString(
                                            "user_mobile_number",
                                            response.getString("mobile_number")
                                        )
                                        .apply()
                                    sharedPreferences.edit()
                                        .putString("user_address", response.getString("address"))
                                        .apply()
                                    sharedPreferences.edit()
                                        .putString("user_email", response.getString("email")).apply()
                                    sessionManager.setLogin(true)
                                    startActivity(
                                        Intent(
                                            this@LoginActivity,
                                            HomeActivity::class.java
                                        )
                                    )
                                    finish()
                                } else {
                                    btnLogin.visibility = View.VISIBLE
                                    txtForgot.visibility = View.VISIBLE
                                    btnLogin.visibility = View.VISIBLE
                                    val errorMessage = data.getString("errorMessage")
                                    Toast.makeText(
                                        this@LoginActivity,
                                        errorMessage,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: JSONException) {
                                btnLogin.visibility = View.VISIBLE
                                txtForgot.visibility = View.VISIBLE
                                txtSignup.visibility = View.VISIBLE
                                e.printStackTrace()
                            }
                        },
                        Response.ErrorListener {
                            btnLogin.visibility = View.VISIBLE
                            txtForgot.visibility = View.VISIBLE
                            txtSignup.visibility = View.VISIBLE
                            Log.e("Error::::", "/post request fail! Error: ${it.message}")
                        }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"

                            /*The below used token will not work, kindly use the token provided to you in the training*/
                            headers["token"] = "d765f26d912196"
                            return headers
                        }
                    }
                    queue.add(jsonObjectRequest)

                }
                else {
                    btnLogin.visibility = View.VISIBLE
                    txtForgot.visibility = View.VISIBLE
                    txtSignup.visibility = View.VISIBLE
                    Toast.makeText(this@LoginActivity, "No internet Connection", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            else {
                btnLogin.visibility = View.VISIBLE
                txtForgot.visibility = View.VISIBLE
                txtSignup.visibility = View.VISIBLE
                Toast.makeText(this@LoginActivity, "Invalid Phone or Password", Toast.LENGTH_SHORT)
                    .show()
            }
            }
        }

    }
