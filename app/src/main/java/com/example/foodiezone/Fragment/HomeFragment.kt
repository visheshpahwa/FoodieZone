package com.example.foodiezone.Fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request.Method.GET
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodiezone.R
import com.example.foodiezone.adapter.HomeRecyclerAdapter
import com.example.foodiezone.model.Restaurant
import com.example.foodiezone.util.ConnectionManager
import org.json.JSONException


class HomeFragment : Fragment() {
    private lateinit var recyclerHome: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    private val restaurantList = arrayListOf<Restaurant>()
    private lateinit var recyclerAdapter:HomeRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_home, container, false)
        // Inflate the layout for this fragment

        recyclerHome=view.findViewById(R.id.recyclerHome)

        layoutManager= LinearLayoutManager(activity)
        progressBar=view.findViewById(R.id.progressBar)
        progressLayout=view.findViewById(R.id.progressLayout)

        progressLayout.visibility=View.VISIBLE


        val queue= Volley.newRequestQueue(activity as Context)
        val url="http://13.235.250.119/v2/restaurants/fetch_result/"
        if (ConnectionManager().checkConnectivity(activity as Context)){
            val jsonObjectRequest=object: JsonObjectRequest(
                GET,url,null,
                Response.Listener {
                    response-> progressLayout.visibility=View.GONE
//                    try{
//                    progressLayout.visibility=View.GONE
//                    val success=it.getBoolean("success")
//
//                    if(success){
//                        val data=it.getJSONArray("data")
//                        for(i in 0 until data.length()){
//                            val bookJsonObject=data.getJSONObject(i)
//                            val bookObject=Book(
//                                bookJsonObject.getString("book_id"),
//                                bookJsonObject.getString("name"),
//                                bookJsonObject.getString("author"),
//                                bookJsonObject.getString("rating"),
//                                bookJsonObject.getString("price"),
//                                bookJsonObject.getString("image")
//                            )
//
//                            bookInfoList.add(bookObject)
//                            recyclerAdapter= DashboardRecyclerAdapter(activity as Context,bookInfoList)
//                            recyclerDashboard.adapter=recyclerAdapter
//                            recyclerDashboard.layoutManager=layoutManager
//
//                        }
//                    }else{
//                        Toast.makeText(activity as Context,"Some error occurred!!!",Toast.LENGTH_SHORT).show()
//                    }
//                }catch(e: JSONException){
//                    Toast.makeText(activity as Context,"Some unexpected error occurred!", Toast.LENGTH_SHORT).show()
//                }
                    try {
                        val data = response.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {

                            val resArray = data.getJSONArray("data")
                            for (i in 0 until resArray.length()) {
                                val resObject = resArray.getJSONObject(i)
                                val restaurant = Restaurant(
                                    resObject.getString("id").toInt(),
                                    resObject.getString("name"),
                                    resObject.getString("rating"),
                                    resObject.getString("cost_for_one").toInt(),
                                    resObject.getString("image_url")
                                )
                                restaurantList.add(restaurant)
                                if (activity != null) {
                                    recyclerAdapter=
                                        HomeRecyclerAdapter(restaurantList, activity as Context)
                                    val mLayoutManager = LinearLayoutManager(activity)
                                    recyclerHome.layoutManager = mLayoutManager
                                    recyclerHome.adapter = recyclerAdapter
                                }

                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }


                },Response.ErrorListener{
                    println("Error is $it")
                    Toast.makeText(activity as Context,"Volley Error Occurred $it", Toast.LENGTH_SHORT).show()
                }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers=HashMap<String,String>()
                    headers["Content-type"]="application/json"
                    headers["token"]="d765f26d912196"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)

        }else{

            val dialog= AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not Found")
            dialog.setPositiveButton("Open Settings"){ text,listner->
                val settingIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text,listner->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }



        return view
    }


}

