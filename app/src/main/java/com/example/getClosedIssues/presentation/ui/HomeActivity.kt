package com.example.getClosedIssues.presentation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.getClosedIssues.R
import com.example.getClosedIssues.utils.extensions.addFragmentWithBackStack
import com.example.getClosedIssues.presentation.ui.landing.LandingFragment

class HomeActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, HomeActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        loadLandingFragment()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val fragmentsCount = supportFragmentManager.backStackEntryCount
        if (fragmentsCount == 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private fun loadLandingFragment() {
        addFragmentWithBackStack(R.id.mainFragmentContainer, LandingFragment.newInstance(), null)
    }
}