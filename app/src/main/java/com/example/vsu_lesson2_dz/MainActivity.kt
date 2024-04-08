package com.example.vsu_lesson2_dz

import android.annotation.SuppressLint
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
                        val text = it.getString(it.getColumnIndex("text"))
                        makeAToastMsg(text)
                        secretKey = text
                    } else {
                        makeAToastMsg("nokey")
                    }
                }
            }
            findViewById<Button>(R.id.broadcastreciever_button) -> {
                makeAToastMsg("b2")
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("CONTENT_PROVIDER_KEY", secretKey)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val contentProviderKey = savedInstanceState?.getString("CONTENT_PROVIDER_KEY")
        if (contentProviderKey != null) {
            Log.d("CONTENT_PROVIDER_KEY", "$contentProviderKey")
            secretKey = contentProviderKey
        }
    }
    fun makeAToastMsg(s: String){
        Toast.makeText(
            this,
            s,
            Toast.LENGTH_SHORT
        ).show()
    }
}