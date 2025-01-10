package com.gs.wialonlocal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwitchText(
    modifier: Modifier = Modifier,
    initial: Boolean = false,
    text: String,
    subTitle: String? = null,
    arrow: Boolean = false,
    onChange: (Boolean) -> Unit = {}
) {
    val checked = rememberSaveable {
        mutableStateOf(initial)
    }
    Column(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.weight(1f)) {
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )
                subTitle?.let {
                    Text(
                        text = subTitle,
                        color = MaterialTheme.colorScheme.inverseSurface,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }


            if(arrow) {
                Icon(
                    Icons.AutoMirrored.Default.KeyboardArrowRight,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = "right",
                    modifier = Modifier.size(50.dp).padding(10.dp)
                )
            } else {
                CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                    Switch(
                        checked = checked.value,
                        onCheckedChange = { isChecked ->
                            checked.value = isChecked
                            onChange(isChecked)
                        },
                        colors = SwitchDefaults.colors(
                            uncheckedThumbColor = MaterialTheme.colorScheme.inverseSurface
                        )
                    )
                }
            }

        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.inverseSurface,
            thickness = 0.5.dp
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RadioText(
    modifier: Modifier = Modifier,
    initial: Boolean = false,
    text: String,
    subTitle: String? = null,
    onChange: (Boolean) -> Unit = {}
) {
    val checked = rememberSaveable(initial) {
        mutableStateOf(initial)
    }
    Column(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.weight(1f)) {
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )
                subTitle?.let {
                    Text(
                        text = subTitle,
                        color = MaterialTheme.colorScheme.inverseSurface,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

                CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                    RadioButton(
                        selected = checked.value,
                        onClick = {
                            onChange(checked.value.not())
                            checked.value = checked.value.not()
                        }
                    )
                }


        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.inverseSurface,
            thickness = 0.5.dp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckText(
    modifier: Modifier = Modifier,
    initial: Boolean = false,
    text: String,
    subTitle: String? = null,
    onChange: (Boolean) -> Unit = {}
) {
    val checked = rememberSaveable {
        mutableStateOf(initial)
    }
    LaunchedEffect(initial) {
        checked.value = initial
    }
    Column(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.surface
        ).clickable {
            onChange(checked.value.not())
            checked.value = checked.value.not()
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.weight(1f)) {
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )
                subTitle?.let {
                    Text(
                        text = subTitle,
                        color = MaterialTheme.colorScheme.inverseSurface,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            if(checked.value) {
                Icon(Icons.Default.Check, contentDescription = "check", tint = MaterialTheme.colorScheme.primary)
            }


        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.inverseSurface,
            thickness = 0.5.dp
        )
    }
}