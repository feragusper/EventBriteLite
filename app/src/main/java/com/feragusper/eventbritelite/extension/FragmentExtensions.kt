package com.feragusper.eventbritelite.extension

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.handleError(message: String?) {
    Log.e(this.javaClass.simpleName, message ?: "Unknown Error")
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}