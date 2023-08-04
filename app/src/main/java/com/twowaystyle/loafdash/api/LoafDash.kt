package com.twowaystyle.loafdash.api

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.twowaystyle.loafdash.model.Breadcrumb
import com.twowaystyle.loafdash.model.SNSProperty

class LoafDash {

    val LOGNAME = "LoafDash"

    val db = Firebase.firestore

    fun getAll() {
        db.collection("breadcrumb")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("MainActivity", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("MainActivity", "Error getting documents.", exception)
            }
    }

    // 自分の近くのパンくずを取得する
    // 近くのユーザ、かつ、会ってないユーザ
    fun getTargetUser() {

    }

    fun postBreadcrumb(breadcrumb: Breadcrumb) {
        db.collection("breadcrumbs")
            .add(breadcrumb)
            .addOnSuccessListener { documentReference ->
                Log.d(LOGNAME, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(LOGNAME, "Error adding document", e)
            }
    }

    @Suppress("UNCHECKED_CAST")
    // as HashMap<String, Double>はキャストできないから@Suppressを記載
    private fun responseToModel(response: QueryDocumentSnapshot): Breadcrumb {
        val id = response.id
        val data: Map<String, Any> = response.data
        val userId: String = data["user_id"] as String

        // FireStoreのgeoPoint型を使用してLocationオブジェクトを作成
        val location: GeoPoint = data["location"] as GeoPoint

        // FireStoreのMap型を使用してSNSPropertyの配列を作成
        val snsProperties: () -> Array<SNSProperty> = {
            val properties: ArrayList<SNSProperty> = arrayListOf()
            val mapProperties: ArrayList<Map<String, String>> = data["sns_profiles"] as ArrayList<Map<String, String>>
            for (mapProperty in mapProperties) {
                val snsType: String = mapProperty["type"] as String
                val snsId: String = mapProperty["id"] as String
                properties.add(SNSProperty(snsType, snsId))
            }
            properties.toTypedArray()
        }

        val profile: String = data["profile"] as String
        val createdAt: Timestamp = data["created_at"] as Timestamp

        return Breadcrumb(
            userId,
            location,
            snsProperties(),
            profile,
            createdAt
        )
    }

}