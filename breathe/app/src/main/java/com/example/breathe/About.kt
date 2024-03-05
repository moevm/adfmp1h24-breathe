package com.example.breathe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.breathe.ui.theme.BreatheTheme

class About : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BreatheTheme {
                AboutLayout(Modifier.fillMaxSize())
            }
        }
    }
}


@Composable
fun AboutLayout(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = modifier)
        {
            AboutHeader()
            Text(
                text = stringResource(R.string.astana_quantum_system),
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(15.dp, 5.dp, 0.dp, 0.dp)
            )
            Spacer(modifier = Modifier.height(25.dp))
            for (i in 0..3) {
                Text(
                    text = stringArrayResource(R.array.about_names)[i],
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .padding(15.dp, 5.dp, 0.dp, 0.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutLayoutPreview() {
    BreatheTheme {
        AboutLayout(Modifier.fillMaxSize())
    }
}