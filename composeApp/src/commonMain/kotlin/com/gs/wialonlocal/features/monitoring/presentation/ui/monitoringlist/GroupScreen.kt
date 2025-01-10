package com.gs.wialonlocal.features.monitoring.presentation.ui.monitoringlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.core.screen.Screen
import org.jetbrains.compose.resources.painterResource
import wialonlocal.composeapp.generated.resources.Res
import wialonlocal.composeapp.generated.resources.check_list

@Composable
fun Groups() {
    Column(Modifier.fillMaxSize()) {
        WorkListEmpty(Modifier.fillMaxSize())
    }
}

@Composable
fun WorkListEmpty(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
       Icon(
           painter = painterResource(Res.drawable.check_list),
           contentDescription = "check icon",
           tint = MaterialTheme.colorScheme.onSurfaceVariant,
           modifier = Modifier.size(56.dp)
       )
        Spacer(Modifier.height(12.dp))
        Text(
            text = strings.workListEmpty,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.W500
            )
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = strings.emptyDescription,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}