package com.margge.carchooser.extensions

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

/**
 * This file contains extensions functions for the project
 */
fun AppCompatActivity.showToast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

inline fun <reified T : Activity> Activity.launchActivity() =
        startActivity(Intent(this, T::class.java))
