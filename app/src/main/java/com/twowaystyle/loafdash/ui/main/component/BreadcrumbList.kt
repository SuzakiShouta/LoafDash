package com.twowaystyle.loafdash.ui.main.component

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.shape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.twowaystyle.loafdash.model.Breadcrumb
import com.twowaystyle.loafdash.model.SNSProperty
import com.twowaystyle.loafdash.ui.theme.Beige
import com.twowaystyle.loafdash.ui.theme.Brown

class BreadcrumbList {

    companion object {

        @Composable
        fun OtherList(list: List<Breadcrumb>) {
            val listState = rememberLazyListState()
            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                state = listState,
            ) {
                list.forEach {
                    item {
                        OtherListRow(
                            it, modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .shadow(
                                    elevation = 6.dp,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(20.dp)
                                )
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
        }

        @Composable
        fun OtherListRow(data: Breadcrumb, modifier: Modifier = Modifier) {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // 表示要素
                    Text(
                        text = data.userName,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 32.sp
                    )// 名前
                    Spacer(modifier = Modifier.size(16.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Beige,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Text(text = "プロフィール")
                        Text(text = data.profile)
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    data.snsProperties.forEach {
                        Spacer(modifier = Modifier.size(8.dp))
                        SNSPropertyRow(
                            it, modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .background(
                                    color = Beige,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .padding(16.dp)
                        )
                    }
                }
                Button(
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Brown,
                        contentColor = Color.White,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Brown,

                        )
                    ,
                    shape = AbsoluteRoundedCornerShape(topLeft = 0.dp, topRight = 0.dp, bottomLeft = 20.dp, bottomRight = 20.dp)
                    ,
                    onClick = { /*TODO*/ }) {
                    Text(
                        text ="Delete",
                        color = Color.White
                    )
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