package com.rj.trendingmovies.apiCall

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.rj.trendingmovies.constants.Constants
import com.rj.trendingmovies.interfaces.CallBackInterface
import com.rj.trendingmovies.utils.Utils
import org.json.JSONObject

class MakeApiCall {
    companion object{

        const val TAG = "MakeApiCall"
        lateinit var callBackListerner: CallBackInterface
        lateinit var errorObj: JSONObject
        fun apiCall( jsonRequestObject: JSONObject?, url:String, context: Context) {
            val requestQueue: RequestQueue? = Volley.newRequestQueue(context)
            val fullUrl = Constants.API_END_POINT_BASE_URL + url

            callBackListerner = context as CallBackInterface
            if(Utils.isOnline(context)){

                val request =
                    JsonObjectRequest(
                        Request.Method.GET, fullUrl, jsonRequestObject,
                        Response.Listener { response ->
                            Log.d("$TAG: ", "Response : $response")
                            if (null != response) callBackListerner.sendResponse(response.toString())
                        },
                        Response.ErrorListener { error ->
                            if (null != error.networkResponse) {
                                errorObj = JSONObject()
                                errorObj.put(Constants.API_RESP_ERROR,"Something went wrong please try again later")
                                callBackListerner.sendResponse(errorObj.toString())
                                Toast.makeText(
                                    context,
                                    "Something went wrong please try again later!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.d(TAG, "Error Response code:$error")
                            }
                        })

                requestQueue?.add(request)
            } else {
                errorObj = JSONObject()
                errorObj.put(Constants.API_RESP_ERROR,"Please check your internet conection")
                callBackListerner.sendResponse(errorObj.toString())
                Toast.makeText(context,"Please check your internet conection", Toast.LENGTH_SHORT).show()
            }
        }

    }
}