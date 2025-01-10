package com.gs.wialonlocal.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun NoPaddingTextField(
    placeholder: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    value: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    onValueChange: (String) -> Unit
) {

    BasicTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = { innerTextField ->
            if(value.isEmpty()) {
                placeholder()
            }
            innerTextField()
        }
    )
}