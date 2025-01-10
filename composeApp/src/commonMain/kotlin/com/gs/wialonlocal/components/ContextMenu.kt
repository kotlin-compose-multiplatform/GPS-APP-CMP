package com.gs.wialonlocal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.MenuDefaults
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

data class ContextButton(
    val text: String,
    val icon: Painter,
    val enabled: Boolean = true,
    val loading: Boolean = false,
    val onClick: () -> Unit
)

@Composable
fun ContextMenu(
    modifier: Modifier = Modifier,
    buttons: List<ContextButton>,
    open: Boolean = false,
    onDismiss: () -> Unit
) {
    DropdownMenu(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.surface
        ),
        expanded = open,
        onDismissRequest = onDismiss,
    ) {
        buttons.forEachIndexed { _, contextButton ->
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            painter = contextButton.icon,
                            contentDescription = contextButton.text,
                            tint = if (contextButton.enabled)
                                MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = contextButton.text,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (contextButton.enabled)
                                MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                onClick = {
                    contextButton.onClick()
                }
            )
        }
    }
}