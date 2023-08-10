package com.twowaystyle.loafdash

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.twowaystyle.loafdash.api.LoafDash
import com.twowaystyle.loafdash.db.SharedPreferencesManager
import com.twowaystyle.loafdash.model.Breadcrumb
import com.twowaystyle.loafdash.model.SNSProperty
import com.twowaystyle.loafdash.sensor.LocationSensor
import com.twowaystyle.loafdash.sensor.LocationUtil
import com.twowaystyle.loafdash.sensor.ShakeNeckEstimation
import java.util.UUID

class MainApplication: Application() {

    val LOGNAME: String = "MainApplication"

    // 自身のデータ
    var userName: MutableState<String> = mutableStateOf("")
    var userId: String = ""
    var snsProperties: MutableList<SNSProperty> = mutableStateListOf(SNSProperty("",""))
    var profile: MutableState<String> = mutableStateOf("")

    // 周囲のパンくず一覧
    private val _targetBreadcrumbs: MutableLiveData<List<Breadcrumb>> = MutableLiveData(listOf())
    val targetBreadcrumbs: LiveData<List<Breadcrumb>> = _targetBreadcrumbs

    fun setTargetBreadcrumbs(value: List<Breadcrumb>) {
        _targetBreadcrumbs.postValue(value)
    }

    // 現在マッチングしてるユーザ
    var encounterUser: Breadcrumb? = null
    // すれ違い済リスト
    var pastEncounterUserIds: MutableList<String> = mutableListOf("")
    fun addPastEncounterUserId(userId: String) {
        pastEncounterUserIds.add(userId)
        if (pastEncounterUserIds.size > 0) {
            sharedPreferencesManager.setPastEncounterUserIds(pastEncounterUserIds.toList())
        }
    }
    // 保存したユーザ
    var keepUsersList: MutableLiveData<List<Breadcrumb>> = MutableLiveData<List<Breadcrumb>>()
    fun addKeepUsersList(breadcrumb: Breadcrumb) {
        val currentList = keepUsersList.value ?: emptyList() // Get the current list or create an empty list
        val updatedList = currentList.toMutableList() // Convert to mutable list for modification
        updatedList.add(breadcrumb) // Add the new data to the mutable list
        keepUsersList.value = updatedList.toList() // Update the LiveData with the modified list
        if (keepUsersList.value != null) {
            sharedPreferencesManager.setKeepUsers(keepUsersList.value!!)
        }
    }
    fun removeKeepUsersListById(userId: String) {
        val currentList = keepUsersList.value ?: emptyList() // Get the current list or create an empty list
        val updatedList = currentList.toMutableList() // Convert to mutable list for modification

        // Find the index of the Breadcrumb with the specified userId
        val indexToRemove = updatedList.indexOfFirst { it.userId == userId }

        if (indexToRemove != -1) {
            updatedList.removeAt(indexToRemove) // Remove the Breadcrumb at the found index
            keepUsersList.value = updatedList.toList() // Update the LiveData with the modified list
        }
        if (keepUsersList.value != null) {
            sharedPreferencesManager.setKeepUsers(keepUsersList.value!!)
        }
    }


    // 最後にパンくずをおいた位置
    var lastBreadcrumbDropGeoPoint: GeoPoint = GeoPoint(0.0,0.0)
    // 最後にパンくずをダウンロードした位置
    var lastBreadcrumbsGetGeoPoint: GeoPoint = GeoPoint(0.0,0.0)

    lateinit var sharedPreferencesManager: SharedPreferencesManager
    lateinit var readOut: ReadOut
    lateinit var locationSensor: LocationSensor
    lateinit var loafDash: LoafDash
    lateinit var shakeNeckEstimation: ShakeNeckEstimation

    fun start(activity: MainActivity) {
        // インスタンス化
        sharedPreferencesManager = SharedPreferencesManager(this)
        readOut = ReadOut(this)
        locationSensor = LocationSensor(activity)
        loafDash = LoafDash(this)
        shakeNeckEstimation = ShakeNeckEstimation.create(this)

        // データ取得、Api
        userId = checkUserId()
        userName.value = sharedPreferencesManager.getUserName()
        snsProperties = sharedPreferencesManager.getSNSProperties().toMutableList()
        profile.value = sharedPreferencesManager.getProfile()
        keepUsersList.postValue(sharedPreferencesManager.getKeepUsers())
        pastEncounterUserIds = sharedPreferencesManager.getPastEncounterUserIds().toMutableList()

        // 権限
        locationSensor.requestPermissions()

        // センサ、推定類
        locationSensor.start()
        shakeNeckEstimation.start()

    }

    // userIdがあれば取得、なければ生成＆保存
    private fun checkUserId (): String {
        var userId = sharedPreferencesManager.getUserId()
        if (userId == "" ) {
            userId = UUID.randomUUID().toString()
            sharedPreferencesManager.setUserId(userId)
            pastEncounterUserIds.add(userId)
        }
        return userId
    }

    fun saveProfile() {
        sharedPreferencesManager.setUserName(userName.value)
        sharedPreferencesManager.setSNSProperties(snsProperties)
        sharedPreferencesManager.setProfile(profile.value)
    }

    fun postBreadcrumb(geoPoint: GeoPoint) {
        lastBreadcrumbDropGeoPoint = geoPoint
//        app.postMyBreadcrumb()
    }

    fun downloadBreadcrumb(geoPoint: GeoPoint) {
        Log.d("MainActivity", "download breadcrumbs")
        lastBreadcrumbDropGeoPoint = geoPoint
//        app.loafDash.getTargetUser(app.locationSensor.geoPoint.value!!, app.pastEncounterUserIds)
        setTargetBreadcrumbs(TestData.breadcrumbs1)
    }

    // マッチングした時
    fun matching(breadcrumb: Breadcrumb) {
        encounterUser = breadcrumb
        Log.d(LOGNAME, "matching, encounter = $breadcrumb")
        val text = breadcrumb.userName +
                "さんとマッチングしました。プロフィールを読み上げます。" +
                breadcrumb.profile +
                "保存しますか？" +
                "首を縦に振ると保存します。首を横に振ると削除します。首を傾げると今回だけ無視します"
        readOut.speechText(text)
    }

    fun keepEncounterUser() {
        if (encounterUser != null) {
            addKeepUsersList(encounterUser!!)
            addPastEncounterUserId(encounterUser!!.userId)
            readOut.speechText("${encounterUser!!.userName}を保存しました。")
            encounterUser = null
        }
    }

    fun notKeepEncounterUser() {
        if (encounterUser != null) {
            addPastEncounterUserId(encounterUser!!.userId)
            readOut.speechText("${encounterUser!!.userName}を保存しませんでした。")
            encounterUser = null
        }
    }

    fun postMyBreadcrumb() {
        if (locationSensor.geoPoint.value != null) {
            lastBreadcrumbDropGeoPoint = locationSensor.geoPoint.value!!
            val myBreadcrumb: Breadcrumb = Breadcrumb(
                userId = userId,
                userName = userName.value,
                location = locationSensor.geoPoint.value!!,
                snsProperties = snsProperties,
                profile = profile.value,
                createdAt = Timestamp.now()
            )
            loafDash.postBreadcrumb(myBreadcrumb)
            Log.d(LOGNAME,"postMyBreadcrumb, $myBreadcrumb")
        }
    }
}