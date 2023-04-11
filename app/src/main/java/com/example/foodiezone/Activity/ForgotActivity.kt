package com.example.foodiezone.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.foodiezone.R

class ForgotActivity : AppCompatActivity() {

    lateinit var etMobileForgot:EditText
    lateinit var etEmailForgot:EditText
    lateinit var btnNextForgot: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)
        title="Forgot Password"

        etMobileForgot=findViewById(R.id.etMobileForgot)
        etEmailForgot=findViewById(R.id.etEmailForgot)
        btnNextForgot=findViewById(R.id.btnNextForgot)


        btnNextForgot.setOnClickListener {

            /*Declaring the intent which sets up the route for the navigation of the activity*/
            val intent = Intent(this@ForgotActivity, HomeActivity::class.java)

            /*Declaring the bundle object which will carry the data*/
            val bundle = Bundle()

            /*Setting a value data which is activity specific. This will be used to identify from where the data was sent*/
            bundle.putString("data", "forgot")

            /*Putting the values in Bundle*/
            bundle.putString("mobile", etMobileForgot.text.toString())
            bundle.putString("email", etEmailForgot.text.toString())

            /*Putting the Bundle to be shipped with the intent*/
            intent.putExtra("details", bundle)

            /*Starting the new activity by sending the intent in the startActivity method*/
            startActivity(intent)
        }
    }
}