package com.twowaystyle.loafdash

import android.app.Application
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.twowaystyle.loafdash.model.Breadcrumb
import com.twowaystyle.loafdash.model.SNSProperty

class MainApplication: Application() {

    var userId: String = ""
    var location: GeoPoint = GeoPoint(1.0, 2.0)
    var snsProperties: Array<SNSProperty> = arrayOf()
    var profile: String = ""
    var createdAt: Timestamp = Timestamp.now()

    fun start() {

    }


}