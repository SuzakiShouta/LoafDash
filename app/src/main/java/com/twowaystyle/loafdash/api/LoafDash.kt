package com.twowaystyle.loafdash.api

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.twowaystyle.loafdash.MainApplication
import com.twowaystyle.loafdash.model.Breadcrumb
import com.twowaystyle.loafdash.model.SNSProperty
import java.util.Calendar

class LoafDash(private val app: MainApplication) {

    private val LOGNAME = "LoafDash"
    private val collectionPass = "breadcrumbs"

    private val db = Firebase.firestore

    fun getAll() {
        db.collection(collectionPass)
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
    fun getTargetUser(geoPoint: GeoPoint, pastEncounterUserIds: List<String>) {
        db.collection(collectionPass)
            .whereNotIn("userId", pastEncounterUserIds) // userIdで絞り込む
            .limit(10) // 上位10件のみ取得
            .get()
            .addOnSuccessListener { querySnapshot ->
                // 受け取った緯度経度+-0.1以下の範囲を計算
                val latUpperBound = geoPoint.latitude + 0.1
                val latLowerBound = geoPoint.latitude - 0.1
                val longUpperBound = geoPoint.longitude + 0.1
                val longLowerBound = geoPoint.longitude - 0.1

                // 緯度経度でさらに絞り込む
                val targetBreadcrumbs = querySnapshot.documents.filter { document ->
                    val location = document.getGeoPoint("location")
                    if (location != null) {
                        val lat = location.latitude
                        val long = location.longitude
                        lat in latLowerBound..latUpperBound && long in longLowerBound..longUpperBound
                    } else {
                        false
                    }
                }

                // Applicationに返すBreadcrumbの配列
                val breadcrumbs: MutableList<Breadcrumb> = mutableListOf()

                // クエリ結果のドキュメントを取得
                for (document in targetBreadcrumbs) {
                    // ドキュメントのデータを取得
                    Log.d(LOGNAME, "get breadcrumb id = ${document.id}")
                    breadcrumbs.add(responseToModel(document))
                }

                // 取得したデータをApplicationに渡す
                app.setTargetBreadcrumbs(breadcrumbs.toList())
            }
            .addOnFailureListener { e ->
                // クエリ失敗時の処理
                println("Error querying documents: $e")
            }
    }

    fun postBreadcrumb(breadcrumb: Breadcrumb) {
        db.collection(collectionPass)
            .add(breadcrumb)
            .addOnSuccessListener { documentReference ->
                Log.d(LOGNAME, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(LOGNAME, "Error adding document", e)
            }
    }

    // 古いドキュメント（FireStoreのカラム）を削除する関数
    fun deleteOldDocuments() {

        // 現在時刻から24時間前のタイムスタンプを計算
        val currentTime = Timestamp.now()
        val twentyFourHoursAgo = Timestamp(currentTime.seconds - (24 * 60 * 60), currentTime.nanoseconds)

        db.collection(collectionPass)
            .whereLessThan("createdAt", twentyFourHoursAgo) // created_atが24時間前よりも前のドキュメントをクエリする
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    Log.d(LOGNAME, "${document.id} => ${document.data}")
                }
                // クエリ結果のドキュメントを順に削除
                for (document in querySnapshot.documents) {
                    document.reference.delete()
                        .addOnSuccessListener {
                            // 成功時の処理
                            println("Document successfully deleted!")
                        }
                        .addOnFailureListener { e ->
                            // 失敗時の処理
                            println("Error deleting document: $e")
                        }
                }
            }
            .addOnFailureListener { e ->
                // クエリ失敗時の処理
                println("Error querying documents: $e")
            }
    }

    // FireStoreのresponseを自作モデルに変換
    @Suppress("UNCHECKED_CAST")
    // as HashMap<String, Double>はキャストできないから@Suppressを記載
    private fun responseToModel(response: DocumentSnapshot): Breadcrumb {
        val id = response.id
        val data: Map<String, Any>? = response.data

        val userName: String = data?.get("userName") as String
        val userId: String = data?.get("userId") as String

        // FireStoreのgeoPoint型を使用してLocationオブジェクトを作成
        val location: GeoPoint = data["location"] as GeoPoint

        // FireStoreのMap型を使用してSNSPropertyの配列を作成
        val snsProperties: List<SNSProperty> = (data["snsProperties"] as? List<Map<String, String>>)
            ?.map { mapProperty ->
                val snsType: String = mapProperty["snsType"] ?: ""
                val snsId: String = mapProperty["snsId"] ?: ""
                SNSProperty(snsType, snsId)
            } ?: emptyList()

        val profile: String = data["profile"] as String
        val createdAt: Timestamp = data["createdAt"] as Timestamp

        return Breadcrumb(
            userId,
            userName,
            location,
            snsProperties,
            profile,
            createdAt
        )
    }



}