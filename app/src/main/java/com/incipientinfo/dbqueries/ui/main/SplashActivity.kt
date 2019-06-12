package com.incipientinfo.dbqueries.ui.main

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.incipientinfo.dbqueries.R
import com.incipientinfo.dbqueries.common.Utility.Companion.SPLASH_DELAY
import com.incipientinfo.dbqueries.common.Utility.Companion.redirectToActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        try {

            redirectToHome()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun redirectToHome() {
        try {

            Handler().postDelayed({
                run {

                    try {
                        redirectToActivity(
                            this@SplashActivity,
                            MainActivity::class.java,
                            applicationContext,
                            true,
                            null
                        )

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }, SPLASH_DELAY)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
