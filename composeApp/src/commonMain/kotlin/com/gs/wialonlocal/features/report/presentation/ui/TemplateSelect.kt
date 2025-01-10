package com.gs.wialonlocal.features.report.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gs.wialonlocal.components.AppError
import com.gs.wialonlocal.components.BackFragment
import com.gs.wialonlocal.components.CheckText
import com.gs.wialonlocal.components.RadioText
import com.gs.wialonlocal.components.SwitchText
import com.gs.wialonlocal.features.main.presentation.ui.SearchBar
import com.gs.wialonlocal.features.report.presentation.viewmodel.ReportViewModel

class TemplateSelect: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: ReportViewModel = navigator.koinNavigatorScreenModel()
        val templateState = viewModel.templateState.collectAsState()
        val selected = viewModel.selectedTemplate
        BackFragment(
            modifier = Modifier.fillMaxSize(),
            title = strings.template,
            onBackPresses = {
                navigator.pop()
            },
            toolbar = {
                Box(Modifier.padding(16.dp).fillMaxWidth()) {
                    SearchBar(
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = strings.search,
                        onSearch = {

                        }
                    )
                }
            }
        ) {
            if(templateState.value.loading) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            } else if(templateState.value.error.isNullOrEmpty().not()) {
                AppError(
                    modifier = Modifier.fillMaxSize(),
                    message = templateState.value.error
                )
            } else {
                templateState.value.templates?.let { list->
                    Column(Modifier.fillMaxSize().background(
                        MaterialTheme.colorScheme.background
                    ).verticalScroll(rememberScrollState())) {
                        repeat(list.count()) { index->
                            val template = list[index]
                            CheckText(
                                modifier = Modifier.fillMaxWidth(),
                                text = template.name,
                                initial = selected.value?.id == template.id,
                                onChange = {
                                    if(it) {
                                        viewModel.setSelectedTemplate(template)
                                        navigator.pop()
                                    } else {
                                        viewModel.setSelectedTemplate(null)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}