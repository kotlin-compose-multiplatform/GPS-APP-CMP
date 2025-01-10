package com.gs.wialonlocal.features.report.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gs.wialonlocal.common.PdfViewer
import com.gs.wialonlocal.features.main.presentation.ui.ToolBar
import com.gs.wialonlocal.features.report.presentation.viewmodel.ReportViewModel
import io.ktor.util.encodeBase64

class ReportView : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = navigator.koinNavigatorScreenModel<ReportViewModel>()
        val reportState = viewModel.reportState.collectAsState()
        val share = remember {
            mutableStateOf<()->Unit>({})
        }
        Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            ToolBar(Modifier.fillMaxWidth()) {
                Row(
                    Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(
                        onClick = {
                            navigator.pop()
                        }
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "close",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Text(
                        "${viewModel.selectedUnit.value?.carNumber} ${viewModel.selectedTemplate.value?.name}",
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.W500,
                            fontSize = 20.sp
                        )
                    )

                    IconButton(
                        onClick = {
                            share.value()
                        }
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "share",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            PdfViewer(
                modifier = Modifier.fillMaxSize(),
                url = "",
                base64 = reportState.value.pdf!!.encodeBase64(),
                byteArray = reportState.value.pdf,
                share = { shareIt->
                    share.value = shareIt
                }
            )

        }
    }
}