package com.example.myapplication.view

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.controller.ConnectivityReceiver
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_INDEFINITE

open class BaseActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private var mSnackBar: Snackbar? = null
    companion object{
        var STATUS = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)


       /* registerReceiver(ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )*/
    }
    @SuppressLint("WrongConstant")
    private fun showMessage(isConnected: Boolean) {

        if (!isConnected) {

            val messageToUser = "You are offline now."
            mSnackBar = Snackbar.make(findViewById(R.id.rootlayout), messageToUser, Snackbar.LENGTH_LONG) //Assume "rootLayout" as the root layout of every activity.
            mSnackBar?.duration = LENGTH_INDEFINITE
            mSnackBar?.show()
            //onStop()

        }
        else {

            mSnackBar?.dismiss()
            Toast.makeText(applicationContext,"this is toast message", Toast.LENGTH_SHORT).show()

        }
    }
    /*override fun onPause() {
        super.onPause()
        unregisterReceiver()
    }*/
    override fun onResume() {
        super.onResume()

        ConnectivityReceiver.connectivityReceiverListener = this

    }
   /* override fun onStop() {
        super.onStop()
        unregisterReceiver(ConnectivityReceiver())
    }*/

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
       showMessage(isConnected)
    }
}
