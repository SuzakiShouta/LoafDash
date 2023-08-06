package com.twowaystyle.loafdash.sensor

import android.location.Location
import android.util.Log
import com.google.firebase.firestore.GeoPoint
import com.twowaystyle.loafdash.model.Breadcrumb
import kotlin.math.*

class LocationUtil {

    companion object {

        // 出会う距離圏内を返す、ないならnull
        fun isEncounter(breadcrumbs: List<Breadcrumb>, currentPosition: GeoPoint): Breadcrumb? {
//            Log.d("LocationUtil", breadcrumbs.toString())
            val encounterRange = 10
            for (breadcrumb in breadcrumbs) {
//                Log.d("LocationUtil", distanceBetweenGeoPoints(currentPosition, breadcrumb.location).toString())
                if ( distanceBetweenGeoPoints(currentPosition, breadcrumb.location) < encounterRange )
                    return breadcrumb
            }
            return null
        }

        // パンくずを落として良い距離かどうか
        fun canDropBreadcrumb (point1: GeoPoint, point2: GeoPoint): Boolean {
            val range = 20
            return distanceBetweenGeoPoints(point1, point2) > range
        }

        // パンくずをダウンロードする必要があるかどうか
        fun isNeedBreadcrumbs (point1: GeoPoint, point2: GeoPoint): Boolean {
            val range = 1000
            return distanceBetweenGeoPoints(point1, point2) > range
        }

        // 緯度経度の二点間の距離をメートルで返す
        fun distanceBetweenGeoPoints(point1: GeoPoint, point2: GeoPoint): Double {
            val results = FloatArray(3)
            Location.distanceBetween(point1.latitude, point1.longitude, point2.latitude, point2.longitude, results)
            return results[0].toDouble()
        }
    }
}