package com.twowaystyle.loafdash.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class Breadcrumb(
    val userId: String,
    val location: GeoPoint,
    val snsProperties: List<SNSProperty>,
    val profile: String,
    val createdAt: Timestamp
)

data class SNSProperty (
    val snsType: String,
    val snsId: String
)
