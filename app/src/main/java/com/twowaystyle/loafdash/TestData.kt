package com.twowaystyle.loafdash

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.twowaystyle.loafdash.model.Breadcrumb
import com.twowaystyle.loafdash.model.SNSProperty

class TestData {
    companion object {
        val breadcrumb1: Breadcrumb = Breadcrumb(
            userId = "qwer-asdf-zxcv-1234",
            userName = "breadcrumb1",
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

        val breadcrumbs1: List<Breadcrumb> = listOf(
            Breadcrumb(
                userId = "aaaa-asdf-zxcv-1234",
                userName = "パンくず太郎",
                location = GeoPoint(35.184782, 137.115550),
                snsProperties = listOf(
                    SNSProperty(
                        snsType = "twitter",
                        snsId = "@pankuzutarooo"
                    )
                ),
                profile = "この近くにいる人繋がりましょう！！！\n 好きなものは強力粉、嫌いなものはピーナッツバターです！ \n 趣味は発酵です。酵母菌系の人は一緒にどうですか？",
                createdAt = Timestamp.now()
            ),
            Breadcrumb(
                userId = "bbbb-asdf-zxcv-1234",
                userName = "breadcrumb1-2",
                location = GeoPoint(35.1, 135.0),
                snsProperties = listOf(
                    SNSProperty(
                        snsType = "twitter",
                        snsId = "@cgdbxhsn"
                    ),
                    SNSProperty(
                        snsType = "instagram",
                        snsId = "cgdbxhsn"
                    ),
                ),
                profile = "breadcrumbs1-2です",
                createdAt = Timestamp.now()
            ),
        )
    }
}