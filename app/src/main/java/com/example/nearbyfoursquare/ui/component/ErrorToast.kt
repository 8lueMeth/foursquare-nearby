package com.example.nearbyfoursquare.ui.component

import android.content.Context
import android.widget.Toast
import com.example.nearbyfoursquare.R

fun errorToast(context: Context) {
    Toast.makeText(
        context,
        context.getText(R.string.network_request_error),
        Toast.LENGTH_SHORT
    ).show()
}