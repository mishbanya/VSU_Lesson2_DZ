package com.example.vsu_lesson2_dz

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), OnClickListener{

    private var secretKey: String? = null
    private var receiverMessage: String? = null
    private val myReceiver = MyBroadcastReceiver()
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val contentProviderButton: Button = findViewById(R.id.contentprovider_button)
        val broadcastReceiverButton: Button = findViewById(R.id.broadcastreciever_button)
        contentProviderButton.setOnClickListener(this)
        broadcastReceiverButton.setOnClickListener(this)
    }

    @SuppressLint("Range")
    override fun onClick(v: View?) {
        when (v){
            findViewById<Button>(R.id.contentprovider_button) -> {
                val uri = Uri.parse("content://dev.surf.android.provider/text")
                val cursor = contentResolver.query(uri, null, null, null, null)
                cursor?.use {
                    if (it.moveToFirst()) {
                        var text = ""
                        if(it.getColumnIndex("text")!=-1){
                            text = it.getString(it.getColumnIndex("text"))
                        }
                        makeAToastMsg(text)
                        secretKey = text
                    } else {
                        makeAToastMsg("nokey")
                    }
                }
            }
            findViewById<Button>(R.id.broadcastreciever_button) -> {
                receiverMessage?.let { makeAToastMsg(it) }
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("CONTENT_PROVIDER_KEY", secretKey)
        outState.putString("BROADCAST_RECIEVER_MSG", receiverMessage)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        secretKey = savedInstanceState?.getString("CONTENT_PROVIDER_KEY")
        receiverMessage = savedInstanceState?.getString("BROADCAST_RECIEVER_MSG")
        secretKey?.let { Log.d("CONTENT_PROVIDER_KEY", it) }
        receiverMessage?.let { Log.d("BROADCAST_RECIEVER_MSG", it) }
    }
    override fun onResume() {
        super.onResume()
        val filter = IntentFilter("ru.shalkoff.vsu_lesson2_2024.SURF_ACTION")
        registerReceiver(myReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(myReceiver)
    }
    fun handleMessage(message: String) {
        receiverMessage = message
        makeAToastMsg(message)
    }
    private fun makeAToastMsg(s: String){
        Toast.makeText(
            this,
            s,
            Toast.LENGTH_SHORT
        ).show()
    }
}