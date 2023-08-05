package com.twowaystyle.loafdash

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.twowaystyle.loafdash.model.Breadcrumb


class MainActivity : AppCompatActivity() {

    lateinit var app: MainApplication
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        app = application as MainApplication

        app.start(this)
        app.testPost()

        app.targetBreadcrumbs.observe(this, Observer {
            for (breadcrumb in it){
                Log.d("MainActivity", breadcrumb.toString())
            }
        })

    }
}