package com.twowaystyle.loafdash

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.firebase.firestore.GeoPoint
import com.twowaystyle.loafdash.ReadOut.Companion.STATE_DONE
import com.twowaystyle.loafdash.model.Breadcrumb
import com.twowaystyle.loafdash.sensor.LocationUtil
import com.twowaystyle.loafdash.sensor.ShakeNeckEstimation.Companion.HEIGHT
import com.twowaystyle.loafdash.sensor.ShakeNeckEstimation.Companion.SLANT
import com.twowaystyle.loafdash.sensor.ShakeNeckEstimation.Companion.WIDTH


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
        app.setTargetBreadcrumbs(TestData.breadcrumbs1)

        // 古いデータを削除
        app.loafDash.deleteOldDocuments()

        app.targetBreadcrumbs.observe(this, Observer {
            for (breadcrumb in it){
                Log.d("MainActivity", breadcrumb.toString())
            }
        })

        app.locationSensor.geoPoint.observe(this, Observer { it ->
            // パンくずを落とす
            if (LocationUtil.canDropBreadcrumb(it, app.lastBreadcrumbDropGeoPoint)){
                app.lastBreadcrumbDropGeoPoint = it
//                app.postMyBreadcrumb()
            }

            // パンくずをダウンロードする
            if (LocationUtil.isNeedBreadcrumbs(it, app.lastBreadcrumbsGetGeoPoint)) {
                app.downloadBreadcrumb(it)
            }

            // マッチング。 パンくずを拾っていて、現在出会っていない時
            if (app.targetBreadcrumbs.value != null && app.encounterUser == null){
                // val isEncounter = LocationUtil.isEncounter(app.targetBreadcrumbs.value!!, app.locationSensor.geoPoint.value!!)
                val isEncounter = LocationUtil.isEncounter(app.targetBreadcrumbs.value!!, GeoPoint(35.184785, 137.115559))
                // 近くに人がいる時
                if (isEncounter != null) {
                    app.matching(isEncounter)
                }
            }

            // 音声読み上げが終わっていて、位置情報が更新されたら、出会ってる人を削除する。
            if (app.readOut.speechState.value == STATE_DONE) {
                app.encounterUser = null
            }
        })

        // 首振り
        app.shakeNeckEstimation.shakeNeck.observe(this, Observer {
            Log.d("MainActivity", "estimation = $it, ${app.encounterUser.toString()}")
            if (app.encounterUser != null) {
                // 首を縦に振った時、ターゲットがいたら保存
                if (it == HEIGHT) {
                    app.keepEncounterUser()
                    app.encounterUser = null
                }
                // 横に振った時、すれ違いリストに追加
                else if (it == WIDTH) {
                    app.addPastEncounterUserId(app.encounterUser!!.userId)
                    app.readOut.speechText("${app.encounterUser!!.userName}を削除しました。")
                    app.encounterUser = null
                }
                // 傾げた時、何もしない
                else if (it == SLANT) {
                    app.keepEncounterUser()
                    app.encounterUser = null
                }
            }
        })

        app.keepUsersList.observe(this, Observer {
            textview.text = it.toString()
        })
    }
}