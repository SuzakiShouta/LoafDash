package com.twowaystyle.loafdash

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.GeoPoint
import com.twowaystyle.loafdash.api.LoafDash
import com.twowaystyle.loafdash.db.SharedPreferencesManager
import com.twowaystyle.loafdash.model.Breadcrumb
import com.twowaystyle.loafdash.model.SNSProperty
import java.util.UUID

class MainApplication: Application() {

    // 自身のデータ
    var userId: String = ""
    var snsProperties: List<SNSProperty> = listOf()
    var profile: String = ""

    // 周囲のパンくず一覧
    private val _targetBreadcrumbs: MutableLiveData<List<Breadcrumb>> = MutableLiveData()
    val targetBreadcrumbs: LiveData<List<Breadcrumb>> = _targetBreadcrumbs

    fun postTargetBreadcrumbs(value: List<Breadcrumb>) {
        _targetBreadcrumbs.postValue(value)
    }

    lateinit var sharedPreferencesManager: SharedPreferencesManager
    lateinit var readOut: ReadOut
    lateinit var locationSensor: LocationSensor
    lateinit var loafDash: LoafDash

    fun start(activity: MainActivity) {
        // インスタンス化
        sharedPreferencesManager = SharedPreferencesManager(this)
        readOut = ReadOut(this)
        locationSensor = LocationSensor(activity)
        loafDash = LoafDash(this)

        // データ取得、Api
        userId = checkUserId()
        snsProperties = sharedPreferencesManager.getSNSProperties()
        profile = sharedPreferencesManager.getProfile()

        // 権限
        locationSensor.requestPermissions()

        // センサ、推定類
        locationSensor.start()

    }

    // userIdがあれば取得、なければ生成＆保存
    private fun checkUserId (): String {
        var userId = sharedPreferencesManager.getUserId()
        if (userId == "" ) {
            userId = UUID.randomUUID().toString()
            sharedPreferencesManager.setUserId(userId)
        }
        return userId
    }

    fun setBreadcrumbs() {

    }


    fun testPost() {
//        loafDash.postBreadcrumb(TestData.breadcrumb1)
        loafDash.getTargetUser(GeoPoint(35.1, 135.0), arrayOf("qwer-asdf-zxcv-1234"))
    }
}