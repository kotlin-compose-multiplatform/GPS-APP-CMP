package com.gs.wialonlocal.features.auth.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gs.wialonlocal.components.BackFragment
import com.gs.wialonlocal.features.auth.data.AuthSettings
import com.gs.wialonlocal.features.auth.presentation.viewmodel.AuthViewModel
import com.gs.wialonlocal.features.global.presentation.ui.GlobalAuthScreen
import com.gs.wialonlocal.features.monitoring.data.settings.UnitsSettings
import com.gs.wialonlocal.state.LocalGlobalNavigator
import org.koin.compose.koinInject

class ProfileScreen : Screen {
    @Composable
    override fun Content() {
        val globalNavigator = LocalGlobalNavigator.current
        val navigator = LocalNavigator.currentOrThrow
        val authSettings: AuthSettings = koinInject()
        val authViewModel: AuthViewModel = navigator.koinNavigatorScreenModel()
        val unitsSettings: UnitsSettings = koinInject()
        Column(Modifier.fillMaxSize()) {
            BackFragment(
                modifier = Modifier.fillMaxWidth(),
                title = strings.profile,
                onBackPresses = {
                    navigator.pop()
                }
            ) {
                Column(Modifier.fillMaxSize().padding(16.dp)) {
                    Text(
                        authSettings.getName(),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W600),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(Modifier.height(22.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        onClick = {
                            authViewModel.logout(
                                onSuccess = {
                                    authSettings.logout()
                                    unitsSettings.saveUnits(emptyList())
                                    globalNavigator.value?.replace(AuthScreen(
                                        onSuccess = {
                                            globalNavigator.value?.replace(GlobalAuthScreen())
                                        }
                                    ))
                                }
                            )

                        }
                    ) {
                        if(authViewModel.logoutLoading.value) {
                            CircularProgressIndicator(Modifier.size(35.dp))
                        } else {
                            Text("Logout")
                        }
                    }
                }
            }
        }
    }
}