package com.gs.wialonlocal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gs.wialonlocal.features.main.presentation.ui.ToolBar

@Composable
fun BackFragment(
    modifier: Modifier = Modifier,
    title: String,
    onBackPresses: () -> Unit,
    toolbar: @Composable () -> Unit = {},
    badge: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize().background(
            color = MaterialTheme.colorScheme.background
        )
    ) {
        ToolBar(Modifier.fillMaxWidth()) {
            Column(Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(
                        onClick = onBackPresses
                    ) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.W500
                        )
                    )

                    badge()
                }
                toolbar()
            }
        }
        content()
    }
}