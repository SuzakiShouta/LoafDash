package com.twowaystyle.loafdash.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.twowaystyle.loafdash.MainApplication
import com.twowaystyle.loafdash.ui.main.component.BreadcrumbList.Companion.ProfileList
import com.twowaystyle.loafdash.ui.main.component.BreadcrumbList.Companion.OtherList
import com.twowaystyle.loafdash.ui.main.component.NoDataView.Companion.NoData
import com.twowaystyle.loafdash.ui.theme.Beige
import com.twowaystyle.loafdash.ui.theme.Brown
import com.twowaystyle.loafdash.ui.theme.LoafDashTheme

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
        val LOGNAME = "MainFragment"
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var app: MainApplication

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        app = requireActivity().application as MainApplication
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val breadcrumbs = app.keepUsersList.observeAsState()
                val profileExpand by remember { app.profileUiExpand }
                app.saveProfile()
                LoafDashTheme{
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Brown
                    ){
                        ConstraintLayout(
                            modifier = Modifier.fillMaxSize()
                        ){
                            Log.d(LOGNAME, "${breadcrumbs.value}")
                            val (column, button) = createRefs()
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                                    .background(Beige, shape = RoundedCornerShape(16.dp))
                                    .wrapContentSize(align = Alignment.Center)
                                    .constrainAs(column){
                                        top.linkTo(parent.top)
                                        bottom.linkTo(parent.bottom)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    }
                            ) {
                                when(profileExpand){
                                    true -> {
                                        ProfileList(
                                            userNameState = app.userName,
                                            profileState = app.profile,
                                            snsList = app.snsProperties
                                        )
                                    }
                                    false -> {
                                        if (breadcrumbs.value != null) {
                                            if (breadcrumbs.value!!.isNotEmpty()) {
                                                OtherList(list = breadcrumbs.value!!)
                                            } else {
                                                Box(
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    NoData()
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            Button(
                                modifier = Modifier
                                    .size(90.dp)
                                    .constrainAs(button) {
                                        bottom.linkTo(parent.bottom, margin = 24.dp)
                                        end.linkTo(parent.end, margin = 24.dp)
                                    },
                                border = BorderStroke(
                                    width = 5.dp,
                                    color = Brown
                                ),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Beige, // ボタンの背景色
                                ),
                                onClick = {
                                    app.profileUiExpand.value = !app.profileUiExpand.value
                                }) {
                                when (profileExpand) {
                                    true -> {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = "",
                                            tint = Brown, // アイコンの色を変数Brownに設定
                                            modifier = Modifier.size(40.dp) // アイコンのサイズを変更
                                        )
                                    }
                                    false -> {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "",
                                            tint = Brown, // アイコンの色を変数Brownに設定
                                            modifier = Modifier.size(40.dp) // アイコンのサイズを変更
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}