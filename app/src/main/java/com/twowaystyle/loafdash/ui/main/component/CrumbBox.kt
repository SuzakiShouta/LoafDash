package com.twowaystyle.loafdash.ui.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.twowaystyle.loafdash.ui.theme.Beige
import com.twowaystyle.loafdash.ui.theme.LoafDashTheme

class CrumbBox {

    companion object {

        @Composable
        fun CrumbBox(
            content: @Composable () -> Unit
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(Beige, shape = RoundedCornerShape(16.dp))
                    .wrapContentSize(align = Alignment.Center)
            ) {
                content()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun NoDataPreview() {
        LoafDashTheme {
            CrumbBox { Text(text = "") }
        }
    }
}