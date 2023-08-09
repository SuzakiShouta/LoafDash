package com.twowaystyle.loafdash.ui.main.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.twowaystyle.loafdash.model.Breadcrumb
import com.twowaystyle.loafdash.model.SNSProperty

class BreadcrumbList {

    companion object {

        @Composable
        fun OtherList(list: List<Breadcrumb>) {
            val listState = rememberLazyListState()
            LazyColumn(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                state = listState,
            ) {
                list.forEach {
                    item {
                        OtherListRow(
                            it, modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .border(
                                    width = 2.dp,
                                    color = Color.DarkGray,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .padding(8.dp)
                        )
                    }
                }
            }

            // リストの終端に近いかどうか
            val listEnd by remember {
                derivedStateOf {
                    listState.firstVisibleItemIndex >= list.size - 15
                }
            }

            // リストの終端に近い場合は追加の検索結果を取得
            if (listEnd) {
//        viewModel.addSearchShop(viewModel.apiParameter)
            }
        }

        @Composable
        fun OtherListRow(data: Breadcrumb, modifier: Modifier = Modifier) {
            // ボタンでその店の詳細を表示
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 表示要素：サムネイル，店舗名，アクセス
                Text(text = data.userName)// 名前
                Spacer(modifier = Modifier.size(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            color = Color.Green,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(8.dp)
                ) {
                    Text(text = "プロフィール")
                    Text(text = data.profile)
                }
                data.snsProperties.forEach {
                    Spacer(modifier = Modifier.size(8.dp))
                    SNSPropertyRow(
                        it, modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .border(
                                width = 2.dp,
                                color = Color.Blue,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.size(8.dp))
                Button(
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    onClick = { /*TODO*/ }) {
                    Text("Delete")
                }
            }
        }

        @Composable
        fun SNSPropertyRow(data: SNSProperty, modifier: Modifier = Modifier) {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(text = "あいこん")
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = data.snsType)
                    Text(text = data.snsId)
                }
            }
        }

        @Preview(showBackground = true)
        @Composable
        fun GreetingPreview() {
            val range = remember { mutableStateOf("0") }

            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                val data = listOf(
                    Breadcrumb(
                        userId = "aaaa-asdf-zxcv-1234",
                        userName = "breadcrumbs1-1",
                        location = GeoPoint(35.184782, 137.115550),
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
                        userName = "breadcrumbs1-2",
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
                    Breadcrumb(
                        "userId",
                        "user",
                        GeoPoint(35.1, 135.0),
                        listOf(
                            SNSProperty("X", "@id"),
                            SNSProperty("Instagram", "@id")
                        ),
                        "プロフィールです",
                        Timestamp.now()
                    ),
                    Breadcrumb(
                        "userId",
                        "userA",
                        GeoPoint(35.1, 135.0),
                        listOf(
                            SNSProperty("X", "@id"),
                            SNSProperty("Instagram", "@id")
                        ),
                        "Aです.\n対よろ．",
                        Timestamp.now()
                    )
                )

                OtherList(list = data)
            }

        }
    }
}