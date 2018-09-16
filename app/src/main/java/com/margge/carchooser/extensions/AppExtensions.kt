package com.margge.carchooser.extensions

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

/**
 * This file contains extensions functions for the project
 */
fun AppCompatActivity.showToast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun AppCompatActivity.launchActivity(cls: Class<*>) =
        startActivity(Intent(this, cls))
