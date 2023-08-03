package com.twowaystyle.loafdash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.twowaystyle.loafdash.model.Breadcrumb


class MainActivity : AppCompatActivity() {

    // 周囲のパンくず一覧
    val breadcrumbs: Array<Breadcrumb> = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 起動時パンくずAPIを叩く
        // 結果をbreadcrumbsへ


    }
}