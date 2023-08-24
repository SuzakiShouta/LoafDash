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
                location = GeoPoint(39.184785, 139.115559),
                snsProperties = listOf(
                    SNSProperty(
                        snsType = "twitter",
                        snsId = "@pankuzutarooo"
                    )
                ),
                profile = "この近くにいる人繋がりましょう！！！\n 好きなものは強力粉、嫌いなものはピーナッツバターです！ \n 趣味は発酵です。酵母菌系の人は一緒にどうですか？",
                createdAt = Timestamp.now()
            ),
        )
        val breadcrumbs2: List<Breadcrumb> = listOf(
            Breadcrumb(
                userId = "1234-asdf-zxcv-1234",
                userName = "食パン 王子",
                location = GeoPoint(35.184785, 137.115559),
                snsProperties = listOf(
                    SNSProperty(
                        snsType = "twitter",
                        snsId = "@shokupan_dash"
                    ),
                    SNSProperty(
                        snsType = "instagram",
                        snsId = "SHOKUPAN_DASH"
                    )
                ),
                profile = "料理が大好きな男子高校生です!\n得意料理はパスタです!運命的な出会いはいつも求めています!\nTwitterのDM待ってます!\n作った料理はレシピと一緒にインスタに投稿してます!",
                createdAt = Timestamp.now()
            ),
            Breadcrumb(
                userId = "aaaa-asdf-zxcv-1234",
                userName = "パンくず太郎",
                location = GeoPoint(39.184785, 139.115559),
                snsProperties = listOf(
                    SNSProperty(
                        snsType = "twitter",
                        snsId = "@pankuzutarooo"
                    )
                ),
                profile = "この近くにいる人繋がりましょう！！！\n 好きなものは強力粉、嫌いなものはピーナッツバターです！ \n 趣味は発酵です。酵母菌系の人は一緒にどうですか？",
                createdAt = Timestamp.now()
            ),
        )
    }
}