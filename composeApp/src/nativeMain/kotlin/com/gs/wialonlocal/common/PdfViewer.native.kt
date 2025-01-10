package com.gs.wialonlocal.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.pin
import kotlinx.cinterop.usePinned
import platform.Foundation.NSCoder
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.dataWithBytes
import platform.Foundation.writeToURL
import platform.PDFKit.PDFDocument
import platform.PDFKit.PDFView
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PdfViewer(
    modifier: Modifier,
    url: String,
    base64: String,
    byteArray: ByteArray?,
    share: (()-> Unit)-> Unit
) {
    val pdfView = rememberSaveable {
        PDFView().apply {
            autoScales = true
        }
    }

    UIKitView(
        factory = {
            pdfView.apply {
                val pdfData = byteArray?.usePinned { pinned ->
                    NSData.dataWithBytes(pinned.addressOf(0), byteArray.size.toULong())
                }
                val document = pdfData?.let { PDFDocument(data = it) }
                pdfView.document = document
            }
            pdfView
        },
        modifier = modifier,
        update = { view->

        },
        properties = UIKitInteropProperties(
            isInteractive = true,
            isNativeAccessibilityEnabled = true
        )
    )

    share {
        sharePdf(byteArray)
    }
}

@OptIn(ExperimentalForeignApi::class)
fun sharePdf(byteArray: ByteArray?) {
    // Ensure the byteArray is not null
    byteArray?.usePinned { pinned ->
        // Convert ByteArray to NSData
        val pdfData = NSData.dataWithBytes(pinned.addressOf(0), byteArray.size.toULong())

        // Write NSData to a temporary file
        val fileManager = NSFileManager.defaultManager
        val tempDir = fileManager.URLsForDirectory(directory = NSDocumentDirectory, inDomains = NSUserDomainMask).firstOrNull() as NSURL
        val tempFileURL = tempDir.URLByAppendingPathComponent("shared.pdf")

        // Write the PDF data to the file
        if (tempFileURL != null) {
            pdfData.writeToURL(tempFileURL, true)
        }

        // Share the PDF file using UIActivityViewController
        val activityViewController = UIActivityViewController(
            activityItems = listOf(tempFileURL),
            applicationActivities = null
        )

        // Get the current top-most view controller
        val rootViewController = getCurrentRootViewController2()

        // Present the share sheet
        rootViewController?.presentViewController(activityViewController, animated = true, completion = null)
    }
}

fun getCurrentRootViewController2(): UIViewController? {
    val keyWindow = UIApplication.sharedApplication.keyWindow
    var rootViewController = keyWindow?.rootViewController

    while (rootViewController?.presentedViewController != null) {
        rootViewController = rootViewController.presentedViewController
    }

    return rootViewController
}