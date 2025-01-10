package com.gs.wialonlocal.features.monitoring.presentation.ui.monitoringlist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gs.wialonlocal.components.ContextButton
import com.gs.wialonlocal.components.ContextMenu
import com.gs.wialonlocal.features.main.presentation.ui.SearchBar
import com.gs.wialonlocal.features.main.presentation.ui.ToolBar
import com.gs.wialonlocal.features.monitoring.presentation.ui.unit.SelectUnits
import com.gs.wialonlocal.state.LocalTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import wialonlocal.composeapp.generated.resources.Res
import wialonlocal.composeapp.generated.resources.check_list
import wialonlocal.composeapp.generated.resources.settings_passive

class MonitoringListScreen: Screen {
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val pagerState = rememberPagerState { 2 }
        val coroutine = rememberCoroutineScope()
        val theme = LocalTheme.current

        val navigator = LocalNavigator.currentOrThrow

        val openSettings = remember {
            mutableStateOf(false)
        }

        val searchQuery = rememberSaveable {
            mutableStateOf("")
        }


        Column(modifier = Modifier.fillMaxSize()) {
            ToolBar {
                Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SearchBar(
                        modifier = Modifier.weight(1f),
                        placeholder = strings.search,
                        onSearch = {query->
                            searchQuery.value = query
                        }
                    )
                    IconButton(
                        onClick = {
                            openSettings.value = true
                        }
                    ) {
                        ContextMenu(
                            open = openSettings.value,
                            onDismiss = {
                                openSettings.value = false
                            },
                            buttons = listOf(
                                ContextButton(
                                    text = strings.selectItems,
                                    icon = painterResource(Res.drawable.check_list),
                                    onClick = {
                                        navigator.push(SelectUnits())
                                    }
                                ),
//                                ContextButton(
//                                    text = strings.configureTabView,
//                                    icon = painterResource(Res.drawable.settings_2),
//                                    onClick = {
//                                        navigator.push(UnitViewSettings())
//                                    }
//                                )
                            )
                        )

                        Icon(
                            painter = painterResource(Res.drawable.settings_passive),
                            contentDescription = "search settings",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                TabRow(
                    pagerState.currentPage,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    indicator = {

                    }
                ) {
                    Tab(pagerState.currentPage == 0, onClick = {
                        coroutine.launch {
                            pagerState.scrollToPage(0)
                        }
                    }, text = {
                        Text(strings.units, color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.bodyLarge)
                    })
                    Tab(pagerState.currentPage == 1, onClick = {
                        coroutine.launch {
                            pagerState.scrollToPage(1)
                        }
                    }, text = {
                        Text(strings.groups, color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.bodyLarge)
                    })
                }
            }

            HorizontalPager(pagerState) { index->
                if(index == 0) {
                    Units(searchQuery.value)
                } else {
                    Groups()
                }
            }
        }
    }
}