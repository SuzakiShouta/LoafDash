package com.twowaystyle.loafdash.sensor

import com.google.firebase.firestore.GeoPoint
import com.twowaystyle.loafdash.model.Breadcrumb
import kotlin.math.*

class LocationUtil {

    companion object {

        // 出会う距離圏内を返す、ないならnull
        fun isEncounter(breadcrumbs: List<Breadcrumb>, currentPosition: GeoPoint): Breadcrumb? {
            val encounterRange = 5
            for (breadcrumb in breadcrumbs) {
                if ( distanceBetweenGeoPoints(currentPosition, breadcrumb.location) < encounterRange )
                    return breadcrumb
            }
            return null
        }

        // パンくずを落として良い距離かどうか
        fun canDropBreadcrumb (point1: GeoPoint, point2: GeoPoint): Boolean {
            val dropRange = 5
            return distanceBetweenGeoPoints(point1, point2) > dropRange
        }

        // 緯度経度の二点間の距離をメートルで返す
        fun distanceBetweenGeoPoints(point1: GeoPoint, point2: GeoPoint): Double {
            val earthRadiusKm = 6371.0

            val lat1 = Math.toRadians(point1.latitude)
            val lon1 = Math.toRadians(point1.longitude)
            val lat2 = Math.toRadians(point2.latitude)
            val lon2 = Math.toRadians(point2.longitude)

            val dLat = lat2 - lat1
            val dLon = lon2 - lon1

            val a = sin(dLat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))

            return earthRadiusKm * c * 1000
        }
    }
}