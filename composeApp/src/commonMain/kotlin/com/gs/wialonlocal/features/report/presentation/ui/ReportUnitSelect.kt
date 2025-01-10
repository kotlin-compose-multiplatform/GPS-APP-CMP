package com.gs.wialonlocal.features.report.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gs.wialonlocal.components.AppError
import com.gs.wialonlocal.components.BackFragment
import com.gs.wialonlocal.components.CheckText
import com.gs.wialonlocal.components.ImageLoader
import com.gs.wialonlocal.components.RadioText
import com.gs.wialonlocal.components.SwitchText
import com.gs.wialonlocal.features.main.presentation.ui.SearchBar
import com.gs.wialonlocal.features.monitoring.domain.model.UnitModel
import com.gs.wialonlocal.features.monitoring.presentation.viewmodel.MonitoringViewModel
import com.gs.wialonlocal.features.report.presentation.viewmodel.ReportViewModel

class ReportUnitSelect: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val monitoringViewModel: MonitoringViewModel = navigator.koinNavigatorScreenModel()
        val viewModel: ReportViewModel = navigator.koinNavigatorScreenModel()
        val selectedUnit = viewModel.selectedUnit
        val units = monitoringViewModel.units.collectAsState()
        val searchQuery = rememberSaveable {
            mutableStateOf("")
        }
        BackFragment(
            modifier = Modifier.fillMaxSize(),
            title = strings.unit,
            onBackPresses = {
                navigator.pop()
            },
            toolbar = {
                Box(Modifier.padding(16.dp).fillMaxWidth()) {
                    SearchBar(
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = strings.search,
                        onSearch = {
                            searchQuery.value = it
                        }
                    )
                }
            }
        ) {
            if(units.value.loading) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            } else if(units.value.error.isNullOrEmpty().not()) {
                AppError(
                    modifier = Modifier.fillMaxSize(),
                    message = units.value.error
                )
            } else {
                Column(Modifier.fillMaxSize().background(
                    MaterialTheme.colorScheme.background
                ).verticalScroll(rememberScrollState())) {
                    units.value.data?.let { list->
                        val filtered = list.filter {
                            it.carNumber.lowercase().contains(searchQuery.value.lowercase()) || it.address.lowercase().contains(searchQuery.value.lowercase()) || searchQuery.value.trim().isEmpty()
                        }
                        repeat(filtered.count()) { index->
                            val unit = filtered[index]
                            ReportSelectCar(
                                modifier = Modifier.fillMaxWidth(),
                                unit = unit,
                                initial = selectedUnit.value?.id == unit.id,
                                onCheckedChange = {
                                    if(it) {
                                        navigator.pop()
                                        viewModel.setSelectedUnit(unit)
                                    } else {
                                        viewModel.setSelectedUnit(null)
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

@Composable
fun ReportSelectCar(
    modifier: Modifier = Modifier,
    initial: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
    unit: UnitModel
) {
    val checked = rememberSaveable {
        mutableStateOf(initial)
    }
    Row(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.surface
        ).clickable {
            onCheckedChange(checked.value.not())
            checked.value = checked.value.not()
        }.padding(vertical = 6.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ImageLoader(
            modifier = Modifier.size(40.dp).clip(CircleShape).border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.inverseSurface,
                shape = CircleShape
            ),
            url = unit.image,
            contentScale = ContentScale.FillBounds
        )

        Spacer(Modifier.width(12.dp))

        Text(
            unit.carNumber,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        if(checked.value) {
            Icon(Icons.Default.Check, contentDescription = "check", tint = MaterialTheme.colorScheme.primary)
        }

    }
}