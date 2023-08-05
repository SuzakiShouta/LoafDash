package com.twowaystyle.loafdash

import android.app.Application
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.twowaystyle.loafdash.db.SharedPreferencesManager
import com.twowaystyle.loafdash.model.Breadcrumb
import com.twowaystyle.loafdash.model.SNSProperty
import java.util.UUID

class MainApplication: Application() {

    var userId: String = ""
    var snsProperties: Array<SNSProperty> = arrayOf()
    var profile: String = ""

    lateinit var sharedPreferencesManager: SharedPreferencesManager
    lateinit var readOut: ReadOut
    lateinit var locationSensor: LocationSensor

    fun start(activity: MainActivity) {
        sharedPreferencesManager = SharedPreferencesManager(this)
        readOut = ReadOut(this)
        locationSensor = LocationSensor(activity)

        userId = checkUserId()
        snsProperties = sharedPreferencesManager.getSNSProperties()
        profile = sharedPreferencesManager.getProfile()

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


}