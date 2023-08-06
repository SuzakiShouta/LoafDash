package com.twowaystyle.loafdash

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.GeoPoint
import com.twowaystyle.loafdash.api.LoafDash
import com.twowaystyle.loafdash.db.SharedPreferencesManager
import com.twowaystyle.loafdash.model.Breadcrumb
import com.twowaystyle.loafdash.model.SNSProperty
import com.twowaystyle.loafdash.sensor.LocationSensor
import java.util.UUID

class MainApplication: Application() {

    val LOGNAME: String = "MainApplication"

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

    // すれ違い済リスト
    var pastEncounterUserIds: List<String> = listOf()

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

    // 自分の現在位置とすれ違い済リストを渡す
    fun setTargetUserBreadcrumbs() {
        loafDash.getTargetUser(GeoPoint(35.1, 135.0), arrayOf("qwer-asdf-zxcv-1234"))
    }


    fun test() {
        // api
//        loafDash.postBreadcrumb(TestData.breadcrumb1)
//        loafDash.getTargetUser(GeoPoint(35.1, 135.0), arrayOf("qwer-asdf-zxcv-1234"))
//        loafDash.deleteOldDocuments()

        // db
//        Log.d(LOGNAME, "${sharedPreferencesManager.getUserId()}")
//
//        sharedPreferencesManager.setSNSProperties(TestData.breadcrumb1.snsProperties)
//        sharedPreferencesManager.setProfile(TestData.breadcrumb1.profile)
//        sharedPreferencesManager.setKeepUsers(TestData.breadcrumbs1)
//
//        Log.d(LOGNAME, "${sharedPreferencesManager.getSNSProperties()}")
//        Log.d(LOGNAME, "${sharedPreferencesManager.getProfile()}")
//
//        for ( keepUser in sharedPreferencesManager.getKeepUsers()) {
//            Log.d(LOGNAME, "keepUser = $keepUser")
//        }
//
//        for ( id in sharedPreferencesManager.getPastEncounterUserIds()) {
//            Log.d(LOGNAME, "id = $id")
//        }
    }
}