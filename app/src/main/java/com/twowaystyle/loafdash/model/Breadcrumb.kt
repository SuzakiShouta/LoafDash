package com.twowaystyle.loafdash.model

data class Breadcrumb(
    val userId: String,
    val location: Location,
    val snsProperties: List<SNSProperty>,
    val profile: String,
    val createdAt: String
)

data class SNSProperty (
    val type: String,
    val id: String
)

data class Location(
    val latitude: Double,
    val longitude: Double
)
