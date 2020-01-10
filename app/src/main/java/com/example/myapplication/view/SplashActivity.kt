package com.example.myapplication.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.view.animation.BounceInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.controller.ConnectivityReceiver
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {


    val ANIMATION_DURATION: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        // Start intro animation.
        /*  try {

          }catch (e:Exception){
              e.printStackTrace()
          }

          registerReceiver(
              ConnectivityReceiver(),
              IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
          )*/

            startAnimation()



        //startAnimation()
    }


    private fun startAnimation() {
        // Intro animation configuration.
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            textTitle.scaleX = value
            textTitle.scaleY = value
            textname.scaleX = value
            textname.scaleY = value
        }
        valueAnimator.interpolator = BounceInterpolator()
        valueAnimator.duration = ANIMATION_DURATION

        // Set animator listener.
        val intent = Intent(this, Registration::class.java)
        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}

            override fun onAnimationEnd(p0: Animator?) {
                // Navigate to main activity on navigation end.
                val animation = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.slide_in)
                animation.start()
                startActivity(intent)
                finish()
            }

            override fun onAnimationCancel(p0: Animator?) {}

            override fun onAnimationStart(p0: Animator?) {}

        })

        // Start animation.
        valueAnimator.start()
    }


}

