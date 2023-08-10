package com.twowaystyle.loafdash.ui.main.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.twowaystyle.loafdash.model.SNSProperty
import kotlinx.coroutines.NonDisposableHandle.parent

class ProfileForm {
    companion object{
        @Composable
        fun ProfileList(list: MutableList<SNSProperty>){
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (column, button) = createRefs()
                LazyColumn(
                    modifier = Modifier
                        .constrainAs(column){
                            top.linkTo(parent.top)
                            bottom.linkTo(button.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    items(count = list.size) {num ->
                        val row = list[num]
                        ProfileListRow(
                            row,
                            typeValueChange = {
                                list[num] = SNSProperty(it, row.snsId)
                            },
                            idValueChange = {
                                list[num] = SNSProperty(row.snsType, it)
                            }
                        )
                    }

                    item{
                        Button(onClick = {
                            list.add(SNSProperty("fb", "@id"))
                            Log.d(javaClass.name, list.toString())
                        }) {
                            Text("add")
                        }
                    }
                }
                IconButton(
                    modifier = Modifier.constrainAs(button){
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    },
                    onClick = {
                        Log.d(javaClass.name, "push: ${list.toList()}")
                    }) {
                    Image(
                        Icons.Default.ArrowBack,
                        contentDescription = "",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.wrapContentSize()
                    )
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
                TextField(
                    value = snsType,
                    onValueChange = {
                        snsType = it
                        typeValueChange.invoke(it)
                    },
                    label = { Text("SNS type")}
                )
                TextField(
                    value = snsId,
                    onValueChange = {
                        snsId = it
                        idValueChange.invoke(it)
                    },
                    label = { Text("SNS id")}
                )
            }
        }
    }
}