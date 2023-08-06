package com.twowaystyle.loafdash

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.firebase.firestore.GeoPoint
import com.twowaystyle.loafdash.model.Breadcrumb
import com.twowaystyle.loafdash.sensor.LocationUtil


class MainActivity : AppCompatActivity() {

    lateinit var app: MainApplication
    lateinit var textview: TextView
    lateinit var textview2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textview = findViewById(R.id.textview_1)
        textview2 = findViewById(R.id.textview_2)

        app = application as MainApplication

        app.start(this)
        app.test()
        app.setTargetBreadcrumbs(TestData.breadcrumbs1)

        // 古いデータを削除
        app.loafDash.deleteOldDocuments()

        app.targetBreadcrumbs.observe(this, Observer {
            for (breadcrumb in it){
                Log.d("MainActivity", breadcrumb.toString())
            }
        })

        app.locationSensor.geoPoint.observe(this, Observer {
            textview.text = "lat = ${app.locationSensor.geoPoint.value?.latitude} \n" +
                    "long = ${app.locationSensor.geoPoint.value?.longitude} \n" +
                    "distance = ${LocationUtil.distanceBetweenGeoPoints(it, app.lastBreadcrumbDropGeoPoint)} \n" +
                    "canDrop = ${LocationUtil.canDropBreadcrumb(it, app.lastBreadcrumbDropGeoPoint)}"
            // パンくずを落とす
            if (LocationUtil.canDropBreadcrumb(it, app.lastBreadcrumbDropGeoPoint)){
                app.lastBreadcrumbDropGeoPoint = it
//                app.postMyBreadcrumb()
            }

            // パンくずをダウンロードする
            if (LocationUtil.isNeedBreadcrumbs(it, app.lastBreadcrumbsGetGeoPoint)) {
                Log.d("MainActivity", "download breadcrumbs")
                app.lastBreadcrumbDropGeoPoint = it
//                app.loafDash.getTargetUser(app.locationSensor.geoPoint.value!!, app.pastEncounterUserIds)
                app.setTargetBreadcrumbs(TestData.breadcrumbs1)
            }

            // マッチング。 パンくずを拾っていて、現在出会っていない時
            if (app.targetBreadcrumbs.value != null && app.encounterUser == null){
                Log.d("MainActivity", "target = ${app.targetBreadcrumbs.value!!}")
                val isEncounter = LocationUtil.isEncounter(app.targetBreadcrumbs.value!!, app.locationSensor.geoPoint.value!!)
//                val isEncounter = LocationUtil.isEncounter(app.targetBreadcrumbs.value!!, GeoPoint(35.184785, 137.115559))
                textview2.text = "encounter = ${isEncounter.toString()}"
                // 近くに人がいる時
                if (isEncounter != null) {
                    app.encounterUser = isEncounter
                    val text = "マッチングしました。プロフィールを読み上げます。" +
                            isEncounter.profile +
                            "保存しますか？"
                    app.readOut.speechText(text)
                }
            }
        })
    }
}