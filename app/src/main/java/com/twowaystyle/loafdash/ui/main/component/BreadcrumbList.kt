package com.twowaystyle.loafdash.ui.main.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.twowaystyle.loafdash.R
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
                val icon = when(data.snsType){
                    "twitter", "Twitter" -> R.drawable.twitter
                    "instagram", "Instagram" -> R.drawable.instagram
                    "discode", "Discode" -> R.drawable.discode
                    "github", "Github", "GitHub" -> R.drawable.github
                    else -> R.drawable.noimage
                }
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White)

                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = data.snsType)
                    Text(text = data.snsId)
                }
            }
        }

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun ProfileList(
            userNameState: MutableState<String>,
            profileState: MutableState<String>,
            snsList: MutableList<SNSProperty>
        ){
            val userName by remember { userNameState }
            val profile by remember { profileState }
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item{
                    TextField(
                        value = userName,
                        onValueChange = {
                            userNameState.value = it
                        },
                        label = { Text(
                            text = "user name",
                            color = Brown
                        )},
                        modifier = Modifier
                            .fillMaxWidth(0.9f),
                        textStyle = TextStyle(fontSize = 24.sp),
                        shape = RoundedCornerShape(20.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Black, // テキストの色
                            containerColor = Color.White, // 背景色
                            cursorColor = Color.Black, // カーソルの色
                            focusedIndicatorColor = Color.Transparent, // フォーカス時のインジケータの色
                            unfocusedIndicatorColor = Color.Transparent, // 非フォーカス時のインジケータの色
                            disabledIndicatorColor = Color.Transparent // 非アクティブ時のインジケータの色
                        )
                    )
                    Spacer(modifier = Modifier.size(8.dp))

                }

                item{
                    TextField(
                        value = profile,
                        onValueChange = {
                            profileState.value = it
                        },
                        label = { Text(
                            text = "profile",
                            color = Brown
                        ) },
                        shape = RoundedCornerShape(20.dp),
                        textStyle = TextStyle(fontSize = 24.sp),
                        modifier = Modifier
                            .fillMaxWidth(0.9f),
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Black, // テキストの色
                            containerColor = Color.White, // 背景色
                            cursorColor = Color.Black, // カーソルの色
                            focusedIndicatorColor = Color.Transparent, // フォーカス時のインジケータの色
                            unfocusedIndicatorColor = Color.Transparent, // 非フォーカス時のインジケータの色
                            disabledIndicatorColor = Color.Transparent // 非アクティブ時のインジケータの色
                        )
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                }

                items(count = snsList.size) { num ->
                    val row = snsList[num]
                    ProfileListRow(
                        row,
                        typeValueChange = {
                            snsList[num] = SNSProperty(it, row.snsId)
                        },
                        idValueChange = {
                            snsList[num] = SNSProperty(row.snsType, it)
                        }
                    )
                }

                item{
                    Button(
                        modifier = Modifier
                            .size(64.dp) // ボタンのサイズを指定
                            .clip(CircleShape), // 角丸ではなく完全な円にする
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Brown, // ボタンの背景色
                            contentColor = Color.White // ボタン内のテキストとアイコンの色
                        ),
                        onClick = {
                            snsList.add(SNSProperty("", ""))
                            Log.d(javaClass.name, snsList.toString())
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add, // アイコンの追加
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp) // アイコンのサイズを指定
                        )
                    }
                }
            }
        }


        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun ProfileListRow(
            data: SNSProperty,
            typeValueChange: (String) -> Unit,
            idValueChange: (String) -> Unit
        ){
            var snsType by remember { mutableStateOf(data.snsType) }
            var snsId by remember { mutableStateOf(data.snsId) }
            Column {
                Spacer(modifier = Modifier.size(8.dp))
                TextField(
                    value = snsType,
                    onValueChange = {
                        snsType = it
                        typeValueChange.invoke(it)
                    },
                    label = { Text(
                        text = "SNS type",
                        color = Brown
                    )},
                    shape = AbsoluteRoundedCornerShape(
                        topLeft = 20.dp,
                        topRight = 20.dp,
                        bottomLeft = 0.dp,
                        bottomRight = 0.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black, // テキストの色
                        containerColor = Color.White, // 背景色
                        cursorColor = Color.Black, // カーソルの色
                        focusedIndicatorColor = Color.Transparent, // フォーカス時のインジケータの色
                        unfocusedIndicatorColor = Color.Transparent, // 非フォーカス時のインジケータの色
                        disabledIndicatorColor = Color.Transparent // 非アクティブ時のインジケータの色
                    )
                )
                TextField(
                    value = snsId,
                    onValueChange = {
                        snsId = it
                        idValueChange.invoke(it)
                    },
                    label = { Text(
                        text = "SNS id",
                        color = Brown
                    )},
                    shape = AbsoluteRoundedCornerShape(
                        topLeft = 0.dp,
                        topRight = 0.dp,
                        bottomLeft = 20.dp,
                        bottomRight = 20.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black, // テキストの色
                        containerColor = Color.White, // 背景色
                        cursorColor = Color.Black, // カーソルの色
                        focusedIndicatorColor = Color.Transparent, // フォーカス時のインジケータの色
                        unfocusedIndicatorColor = Color.Transparent, // 非フォーカス時のインジケータの色
                        disabledIndicatorColor = Color.Transparent // 非アクティブ時のインジケータの色
                    )
                )
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