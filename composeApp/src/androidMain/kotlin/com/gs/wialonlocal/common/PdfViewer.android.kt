package com.gs.wialonlocal.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.rememberVerticalPdfReaderState
import java.io.File

private fun shareFile(file: File, context: Context) {
    val uri: Uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
    val intent = ShareCompat.IntentBuilder.from(context as Activity)
        .setType("application/pdf")
        .setStream(uri)
        .createChooserIntent()
        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    context.startActivity(intent)
}

@Composable
actual fun PdfViewer(
    modifier: Modifier,
    url: String,
    base64: String,
    byteArray: ByteArray?,
    share: (()-> Unit)-> Unit
) {
    val context = LocalContext.current
    val pdfState = rememberVerticalPdfReaderState(
        resource = ResourceType.Base64(base64),
        isZoomEnable = true,
    )

    share {
        pdfState.file?.let {
            shareFile(it, context)
        }
    }

    VerticalPDFReader(
        state = pdfState,
        modifier = modifier,
    )
}