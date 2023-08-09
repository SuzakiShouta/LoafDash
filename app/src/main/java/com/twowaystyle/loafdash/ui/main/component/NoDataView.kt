package com.twowaystyle.loafdash.ui.main.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

class NoDataView {
    companion object {
        @Composable
        fun NoData() {
            Text(
                text = "携帯を口に咥えて走ろう！",
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun NoDataPreview() {
        NoData()
    }
}