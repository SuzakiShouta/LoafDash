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

        val breadcrumbs1: List<Breadcrumb> = listOf(
            Breadcrumb(
                userId = "aaaa-asdf-zxcv-1234",
                location = GeoPoint(35.1, 135.0),
                snsProperties = listOf(
                    SNSProperty(
                        snsType = "twitter",
                        snsId = "@mjnhbgvf"
                    )
                ),
                profile = "breadcrumbs1-1です",
                createdAt = Timestamp.now()
            ),
            Breadcrumb(
                userId = "bbbb-asdf-zxcv-1234",
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