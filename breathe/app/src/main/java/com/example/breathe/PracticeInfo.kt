package com.example.breathe

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.breathe.ui.theme.BreatheTheme

@Composable
fun PracticeInfoLayout(modifier: Modifier = Modifier) {

}

@Preview(showBackground = true)
@Composable
fun PracticeInfoLayoutPreview() {
    BreatheTheme {
        PracticeInfoLayout(Modifier.fillMaxSize())
    }
}
