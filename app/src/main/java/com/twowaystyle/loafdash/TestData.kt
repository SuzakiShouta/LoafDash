package com.twowaystyle.loafdash

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.twowaystyle.loafdash.model.Breadcrumb
import com.twowaystyle.loafdash.model.SNSProperty

class TestData {
    companion object {
        val breadcrumb1: Breadcrumb = Breadcrumb(
            userId = "qwer-asdf-zxcv-1234",
            location = GeoPoint(35.1, 135.0),
            snsProperties = listOf(
                SNSProperty(
                    snsType = "twitter",
                    snsId = "@q12we34r"
                )
            ),
            profile = "breadcrumb1です",
            createdAt = Timestamp.now()
        )
    }
}