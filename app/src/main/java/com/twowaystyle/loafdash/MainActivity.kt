package com.twowaystyle.loafdash

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import com.google.firebase.firestore.GeoPoint
import com.twowaystyle.loafdash.ReadOut.Companion.STATE_DONE
import com.twowaystyle.loafdash.model.Breadcrumb
import com.twowaystyle.loafdash.sensor.LocationUtil
import com.twowaystyle.loafdash.sensor.ShakeNeckEstimation.Companion.HEIGHT
import com.twowaystyle.loafdash.sensor.ShakeNeckEstimation.Companion.SLANT
import com.twowaystyle.loafdash.sensor.ShakeNeckEstimation.Companion.WIDTH
import com.twowaystyle.loafdash.ui.main.MainFragment
import com.twowaystyle.loafdash.ui.theme.LoafDashTheme


class MainActivity : AppCompatActivity() {

    lateinit var app: MainApplication

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
//            if (app.targetBreadcrumbs.value != null && app.encounterUser == null){
//                // val isEncounter = LocationUtil.isEncounter(app.targetBreadcrumbs.value!!, app.locationSensor.geoPoint.value!!)
//                val isEncounter = LocationUtil.isEncounter(app.targetBreadcrumbs.value!!, GeoPoint(35.184785, 137.115559))
//                // 近くに人がいる時
//                if (isEncounter != null) {
//                    app.matching(isEncounter)
//                }
//            }

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
                    app.notKeepEncounterUser()
                }
                // 傾げた時、何もしない
                else if (it == SLANT) {
                    app.keepEncounterUser()
                    app.encounterUser = null
                }
            }
        })

        setContent{
            LoafDashTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 初期画面の検索条件の設定画面のFragmentを表示
                    Scaffold(
                        content = {
                            AndroidView(factory = { context ->
                                FragmentContainerView(context).apply {
                                    id = View.generateViewId()
                                }
                            }, update = {
                                val fragment = MainFragment.newInstance()
                                val transaction = supportFragmentManager.beginTransaction()
                                transaction.replace(it.id, fragment)
                                transaction.commit()
                            })
                        },
                    )
                }
            }
        }


    }
}