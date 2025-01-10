package com.gs.wialonlocal.features.global.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.gs.wialonlocal.common.getStoreUrl
import org.jetbrains.compose.resources.painterResource
import wialonlocal.composeapp.generated.resources.Res
import wialonlocal.composeapp.generated.resources.ytm_green

@Composable
fun UpdateAppComponent(version: String) {
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(Res.drawable.ytm_green),
            contentDescription = "logo",
            modifier = Modifier.size(200.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Please update your app! New version is $version",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                uriHandler.openUri(getStoreUrl())
            }
        ) {
            Text("Update app",  style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}