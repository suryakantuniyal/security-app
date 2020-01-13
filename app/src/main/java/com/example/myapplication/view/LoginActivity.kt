package com.example.myapplication.view

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.controller.PreferenceHelper
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    internal var loginUrl = "https://demonuts.com/Demonuts/JsonTest/Tennis/simpleregister.php"


    private var username: TextInputEditText? = null
    private var userpassword: TextInputEditText? = null
    private val LoginTask = 1
    private var mProgressDialog: ProgressDialog? = null
    private var preferenceHelper: PreferenceHelper? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        username = findViewById<TextInputEditText>((R.id.userlogin_edt))
        userpassword = findViewById(R.id.passlogin_edt)
        val login_btn = findViewById(R.id.login_btn) as Button

        login_btn?.setOnClickListener {

            try {
                login()
            } catch (e: IOException) {
                e.printStackTrace()
            }


        }
    }

    private fun login() {

        showSimpleProgressDialog(this@LoginActivity, null, "Please wait...", false)
        try {
            Fuel.post(
                loginUrl, listOf(
                    "username" to username!!.text.toString()
                    , "userpassword" to userpassword!!.text.toString()
                )
            ).responseJson { request, response, result ->
                onTaskCompleted(result.get().content, LoginTask)
            }

        } catch (e: Exception) {
        }
    }

    private fun onTaskCompleted(response: String, loginTask: Int) {
        removeProgressDialog()
        when (loginTask) {
            LoginTask -> if (isSuccess(response)) {
                saveInfo(response)
                Toast.makeText(this@LoginActivity, "Login Successfully!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity, WelcomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                val animation = AnimationUtils.loadAnimation(this, R.anim.slide_in)
                animation.start()
                startActivity(intent)
                this.finish()
            }
            /* else {
                 Toast.makeText(this@LoginActivity, getErrorMessage(response), Toast.LENGTH_SHORT).show()
             }
             */
        }

    }


    private fun saveInfo(response: String) {

        preferenceHelper!!.putIsLogin(true)

        try {
            val jsonObject = JSONObject(response)
            if (jsonObject.getString("status") == "true") {
                val dataArray = jsonObject.getJSONArray("data")
                for (i in 0 until dataArray.length()) {
                    val dataObj = dataArray.getJSONObject(i)
                    preferenceHelper!!.putName(dataObj.getString("name"))
                    preferenceHelper!!.putLocation(dataObj.getString("location"))
                }

            }

        } catch (e: JSONException) {
        }

    }

    private fun isSuccess(response: String): Boolean {
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

    private fun removeProgressDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

}
