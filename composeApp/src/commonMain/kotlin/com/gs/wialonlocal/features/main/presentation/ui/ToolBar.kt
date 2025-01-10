package com.gs.wialonlocal.features.main.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.gs.wialonlocal.components.NoPaddingTextField

@Composable
fun ToolBar(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth().background(
            color = MaterialTheme.colorScheme.primary
        )
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    placeholder: String,
    onSearch: (String) -> Unit
) {
    val query = rememberSaveable {
        mutableStateOf("")
    }
    val shape = RoundedCornerShape(2.dp)

    Row(
        modifier = modifier.clip(shape).shadow(2.dp, shape, true).background(
            color = MaterialTheme.colorScheme.surface,
            shape = shape
        ).padding(
            vertical = 12.dp,
            horizontal = 12.dp
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            Icons.Default.Search,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            contentDescription = placeholder,
            modifier = Modifier.size(20.dp)
        )
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            NoPaddingTextField(
                value = query.value,
                onValueChange = {
                    query.value = it
                    if (it.trim().isEmpty()) {
                        onSearch(it)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearch(query.value)
                    }
                ),
                placeholder = {
                    Text(
                        placeholder,
                        color = MaterialTheme.colorScheme.outline,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                modifier = Modifier.weight(1f).padding(0.dp)
            )
        }


    }


}