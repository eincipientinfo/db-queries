package com.incipientinfo.dbqueries.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.incipientinfo.dbqueries.R
import com.incipientinfo.dbqueries.ui.home.HomeFragment
import com.incipientinfo.dbqueries.ui.querieslist.QueriesListFragment


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    companion object {
        @SuppressLint("StaticFieldLeak")
        var navView: BottomNavigationView? = null
    }

    private var doubleBackToExit = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            loadFragment(HomeFragment())

            navView = findViewById(R.id.nav_view)

            navView!!.setOnNavigationItemSelectedListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private fun loadFragment(fragment: Fragment?): Boolean {
        //switching fragment
        try {
            if (fragment != null) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frgContainer, fragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        navView!!.menu.findItem(item.itemId).isChecked = true

        when (item.itemId) {
            R.id.navigation_home -> {
                fragment = HomeFragment()
            }

            R.id.navigation_dashboard -> {
                fragment = QueriesListFragment()
            }

        }

        return loadFragment(fragment)
    }


    fun navigationTab(pos: Int) {
        try {
            if (pos == 0) {
                navView!!.menu.findItem(R.id.navigation_home).isChecked = true
            } else if (pos == 1) {
                navView!!.menu.findItem(R.id.navigation_dashboard).isChecked = true

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        try {
            if (doubleBackToExit) {
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
                System.exit(0)
                return
            }

            this.doubleBackToExit = true
            Toast.makeText(this, "press BACK again to exit", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ doubleBackToExit = false }, 2000)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
