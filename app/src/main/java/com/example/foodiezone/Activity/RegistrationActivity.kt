package com.example.foodiezone.Activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodiezone.R
import com.example.foodiezone.util.ConnectionManager
import com.example.foodiezone.util.REGISTER
import com.example.foodiezone.util.SessionManager
import com.example.foodiezone.util.Validations
import org.json.JSONObject
import java.lang.Exception

class RegistrationActivity : AppCompatActivity() {

    lateinit var etNameReg:EditText
    lateinit var etMobileReg:EditText
    lateinit var etEmailReg:EditText
    lateinit var etDeliveryAddressReg:EditText
    lateinit var etPasswordReg:EditText
    lateinit var etConfirmPasswordReg:EditText
    lateinit var btnReg:Button
    lateinit var sharedPreferences: SharedPreferences
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        title="Register Yourself!"

        sessionManager= SessionManager(this@RegistrationActivity)
        sharedPreferences=this@RegistrationActivity.getSharedPreferences(sessionManager.PREF_NAME,sessionManager.PRIVATE_MODE)
        etNameReg=findViewById(R.id.etNameReg)
        etMobileReg=findViewById(R.id.etMobileReg)
        etEmailReg=findViewById(R.id.etEmailReg)
        etDeliveryAddressReg=findViewById(R.id.etDeliveryAddressReg)
        etPasswordReg=findViewById(R.id.etPasswordReg)
        etConfirmPasswordReg=findViewById(R.id.etConfirmPasswordReg)
        btnReg=findViewById(R.id.btnReg)

        btnReg.setOnClickListener {
            if (Validations.validateNameLength(etNameReg.text.toString())) {
                etNameReg.error = null
                if (Validations.validateEmail(etEmailReg.text.toString())) {
                    etEmailReg.error = null
                    if (Validations.validateMobile(etMobileReg.text.toString())) {
                        etMobileReg.error = null
                        if (Validations.validatePasswordLength(etPasswordReg.text.toString())) {
                            etPasswordReg.error = null
                            if (Validations.matchPassword(
                                    etPasswordReg.text.toString(),
                                    etConfirmPasswordReg.text.toString()
                                )
                            ) {
                                etPasswordReg.error = null
                                etConfirmPasswordReg.error = null
                                if (ConnectionManager().isNetworkAvailable(this@RegistrationActivity)) {
                                    sendRegisterRequest(
                                        etNameReg.text.toString(),
                                        etMobileReg.text.toString(),
                                        etDeliveryAddressReg.text.toString(),
                                        etPasswordReg.text.toString(),
                                        etEmailReg.text.toString()
                                    )
                                }
//                                } else {
//                                    rlRegister.visibility = View.VISIBLE
//                                    progressBar.visibility = View.INVISIBLE
//                                    Toast.makeText(this@RegisterActivity, "No Internet Connection", Toast.LENGTH_SHORT)
//                                        .show()
//                                }
                            } else {
//                                rlRegister.visibility = View.VISIBLE
//                                progressBar.visibility = View.INVISIBLE
                                etPasswordReg.error = "Passwords don't match"
                                etConfirmPasswordReg.error = "Passwords don't match"
                                Toast.makeText(this@RegistrationActivity, "Passwords don't match", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {
//                            rlRegister.visibility = View.VISIBLE
//                            progressBar.visibility = View.INVISIBLE
                            etPasswordReg.error = "Password should be more than or equal 4 digits"
                            Toast.makeText(
                                this@RegistrationActivity,
                                "Password should be more than or equal 4 digits",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
//                        rlRegister.visibility = View.VISIBLE
//                        progressBar.visibility = View.INVISIBLE
                        etMobileReg.error = "Invalid Mobile number"
                        Toast.makeText(this@RegistrationActivity, "Invalid Mobile number", Toast.LENGTH_SHORT).show()
                    }
                } else {
//                    rlRegister.visibility = View.VISIBLE
//                    progressBar.visibility = View.INVISIBLE
                    etEmailReg.error = "Invalid Email"
                    Toast.makeText(this@RegistrationActivity, "Invalid Email", Toast.LENGTH_SHORT).show()
                }
            } else {
//                rlRegister.visibility = View.VISIBLE
//                progressBar.visibility = View.INVISIBLE
                etNameReg.error = "Invalid Name"
                Toast.makeText(this@RegistrationActivity, "Invalid Name", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun sendRegisterRequest(name: String, phone: String, address: String, password: String, email: String) {

        val queue = Volley.newRequestQueue(this)

        val jsonParams = JSONObject()
        jsonParams.put("name", name)
        jsonParams.put("mobile_number", phone)
        jsonParams.put("password", password)
        jsonParams.put("address", address)
        jsonParams.put("email", email)

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST,
            REGISTER,
            jsonParams,
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
                                this@RegistrationActivity,
                                HomeActivity::class.java
                            )
                        )
                        finish()
                    } else {
//                        rlRegister.visibility = View.VISIBLE
//                        progressBar.visibility = View.INVISIBLE
                        val errorMessage = data.getString("errorMessage")
                        Toast.makeText(
                            this@RegistrationActivity,
                            errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception){
//                    rlRegister.visibility = View.VISIBLE
//                    progressBar.visibility = View.INVISIBLE
                    e.printStackTrace()
                }
            },
            Response.ErrorListener {
                Toast.makeText(this@RegistrationActivity, it.message, Toast.LENGTH_SHORT).show()
//                rlRegister.visibility = View.VISIBLE
//                progressBar.visibility = View.INVISIBLE
            }
        ){
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
    override fun onSupportNavigateUp(): Boolean {
        Volley.newRequestQueue(this).cancelAll(this::class.java.simpleName)
        onBackPressed()
        return true
    }
}