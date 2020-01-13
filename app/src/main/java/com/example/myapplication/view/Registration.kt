package com.example.myapplication.view

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import com.example.myapplication.R
import com.example.myapplication.controller.PreferenceHelper
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.net.CacheResponse

class Registration : AppCompatActivity() {

    internal var RegisterUrl = "http://192.168.5.14:8000/api/register"
    private var username: EditText? = null
    private var userLocation: AutoCompleteTextView? = null
    private var userpassword: EditText? = null
    private var preferenceHelper: PreferenceHelper? = null
    private var mProgressDialog: ProgressDialog? = null
    private var register_btn: Button? = null
    private val RegTask = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        preferenceHelper = PreferenceHelper(this)

        username = findViewById<View>(R.id.name_edt) as EditText
        userLocation = findViewById(R.id.location_edt) as AutoCompleteTextView
        userpassword = findViewById(R.id.passkey_edt) as EditText
        register_btn = findViewById(R.id.register_btn) as Button
        val login_tv = findViewById(R.id.login_tv) as TextView

        val indian_states = resources.getStringArray(R.array.india_states)

        val adapter = ArrayAdapter(this,
            android.R.layout.simple_expandable_list_item_1,indian_states)
        userLocation!!.setAdapter<ArrayAdapter<String>?>(adapter)


        login_tv?.setOnClickListener {
            val intent = Intent(this@Registration, LoginActivity::class.java)

            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_in)
            animation.start()
            startActivity(intent)
        }
        register_btn!!.setOnClickListener {
            registration()
        }

    }

    private fun registration() {
        showSimpleProgressDialog(this@Registration, null, "Please wait...", false)

        try {
            Fuel.post(
                RegisterUrl, listOf(
                    "name" to username!!.text.toString()
                    ,"email" to "surya@cynotech.com"
                    , "password" to userpassword!!.text.toString()
                    , "location" to userLocation!!.text.toString()
                    ,"password_confirmation" to "admin@123"
                )
            ).responseJson { request, response, result ->
                Log.d("data", result.get().content)
                onTaskCompleted(result.get().content, RegTask)
            }

        } catch (e: Exception) {

        } finally {

        }
    }

    private fun onTaskCompleted(response: String, task: Int) {
        removeSimpleProgressDialog()
        when (task) {
            RegTask -> if (isSuccess(response)) {
                saveInfo(response)
                Toast.makeText(this@Registration, "Registered Successfully!", Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(this@Registration, WelcomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                val animation = AnimationUtils.loadAnimation(this, R.anim.slide_in)
                animation.start()
                startActivity(intent)
                this.finish()
            } else {
                Toast.makeText(this@Registration, getErrorMessage(response), Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

 fun saveInfo(response: String) {
         preferenceHelper!!.putIsLogin(true)
         try {

             val jsonObject = JSONObject(response)
             if (jsonObject.getString("status") == "true") {
                 val dataArray = jsonObject.getJSONArray("data")
                 for (i in 0 until dataArray.length()) {

                     val dataobj = dataArray.getJSONObject(i)
                     preferenceHelper!!.putName(dataobj.getString("name"))
                     preferenceHelper!!.putLocation(dataobj.getString("location"))
                 }
             }
         } catch (e: JSONException) {
             e.printStackTrace()
         }

     }


    fun isSuccess(response: String): Boolean {
        try {
            val jsonObject = JSONObject(response)
            return if (jsonObject.optString("status") == "true") {
                true
            } else {

                false
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return false
    }

    fun getErrorMessage(response: String): String {
        try {
            val jsonObject = JSONObject(response)
            return jsonObject.getString("message")

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return "No data"
    }

    fun showSimpleProgressDialog(
        context: Context,
        title: String?,
        msg: String,
        isCancelable: Boolean
    ) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg)
                mProgressDialog!!.setCancelable(isCancelable)
            }
            if (!mProgressDialog!!.isShowing) {
                mProgressDialog!!.show()
            }

        } catch (ie: IllegalArgumentException) {
            ie.printStackTrace()
        } catch (re: RuntimeException) {
            re.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog!!.isShowing) {
                    mProgressDialog!!.dismiss()
                    mProgressDialog = null
                }
            }
        } catch (ie: IllegalArgumentException) {
            ie.printStackTrace()

        } catch (re: RuntimeException) {
            re.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}
