package com.example.breathe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.breathe.ui.theme.BreatheTheme

@Composable
fun AboutLayout(
    modifier: Modifier = Modifier,
    upButton: (()->Unit)? = null
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = modifier)
        {
            BackHeader(
                title = stringResource(R.string.about),
                upButton = upButton
            )
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

