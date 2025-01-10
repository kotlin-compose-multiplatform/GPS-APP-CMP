package com.gs.wialonlocal.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
expect fun PdfViewer(
    modifier: Modifier = Modifier, url: String, base64: String, byteArray: ByteArray?,  share: (()-> Unit)-> Unit)