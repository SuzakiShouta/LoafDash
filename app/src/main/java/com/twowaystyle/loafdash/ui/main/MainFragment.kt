package com.twowaystyle.loafdash.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.livedata.observeAsState
import com.twowaystyle.loafdash.MainApplication
import com.twowaystyle.loafdash.R
import com.twowaystyle.loafdash.ui.main.component.BreadcrumbList
import com.twowaystyle.loafdash.ui.main.component.BreadcrumbList.Companion.OtherList
import com.twowaystyle.loafdash.ui.main.component.NoDataView.Companion.NoData
import com.twowaystyle.loafdash.ui.theme.LoafDashTheme

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
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
                LoafDashTheme{
                    Surface(
                        modifier = Modifier.fillMaxSize()
                    ){
                        if (breadcrumbs.value != null) {
                            OtherList(list = breadcrumbs.value!!)
                        } else {
                            NoData()
                        }
                    }
                }
            }
        }
    }

}