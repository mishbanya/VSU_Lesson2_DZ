package com.example.vsu_lesson2_dz

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "ru.shalkoff.vsu_lesson2_2024.SURF_ACTION") {
            val message = intent.getStringExtra("message")
            if (message != null) {
                Log.d("MyBroadcastReceiver", "Received message: $message")
                (context as? MainActivity)?.handleMessage(message)
            }
        }
    }
}