import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.ProvideStrings
import cafe.adriel.lyricist.rememberStrings
import cafe.adriel.voyager.navigator.Navigator
import coil3.compose.rememberAsyncImagePainter
import com.gs.wialonlocal.common.getDevice
import com.gs.wialonlocal.common.getVersion
import com.gs.wialonlocal.components.AppError
import com.gs.wialonlocal.components.AppLoading
import com.gs.wialonlocal.core.network.provideHttpClient
import com.gs.wialonlocal.core.network.provideSettings
import com.gs.wialonlocal.core.network.provideViewModel
import com.gs.wialonlocal.features.auth.data.AuthSettings
import com.gs.wialonlocal.features.auth.di.authModule
import com.gs.wialonlocal.features.auth.presentation.ui.AuthScreen
import com.gs.wialonlocal.features.geofence.di.geoFenceModule
import com.gs.wialonlocal.features.global.di.globalModule
import com.gs.wialonlocal.features.global.presentation.ui.GlobalAuthScreen
import com.gs.wialonlocal.features.global.presentation.ui.UpdateAppComponent
import com.gs.wialonlocal.features.global.presentation.viewmodel.GlobalViewModel
import com.gs.wialonlocal.features.main.presentation.ui.MainScreen
import com.gs.wialonlocal.features.monitoring.di.monitoringModule
import com.gs.wialonlocal.features.report.di.reportModule
import com.gs.wialonlocal.features.settings.data.settings.AppSettings
import com.gs.wialonlocal.features.settings.data.settings.AppTheme
import com.gs.wialonlocal.features.settings.di.settingsModule
import com.gs.wialonlocal.state.LocalAppSettings
import com.gs.wialonlocal.state.LocalGlobalNavigator
import com.gs.wialonlocal.state.LocalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

import org.koin.compose.KoinApplication
import com.gs.wialonlocal.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import org.koin.dsl.koinApplication
import wialonlocal.composeapp.generated.resources.Res
import wialonlocal.composeapp.generated.resources.monitoring_active
import wialonlocal.composeapp.generated.resources.ytm_green

@Composable
@Preview
fun App(
    context: Any? = null
) {
    KoinContext(
        context = koinApplication {
            modules(
                listOf(
                    provideHttpClient,
                    provideViewModel,
                    provideSettings,
                    monitoringModule,
                    authModule,
                    globalModule,
                    reportModule,
                    geoFenceModule,
                    settingsModule
                )
            )
        }.koin
    ) {
        val authSettings = koinInject<AuthSettings>()

        LaunchedEffect(true) {
            authSettings.getToken()
        }

        val globalViewModel: GlobalViewModel = koinInject()

        val versionState = globalViewModel.versionState.collectAsState()

        LaunchedEffect(true) {
            globalViewModel.initVersion()
        }

        val settings = koinInject<AppSettings>()



        val localSettings = remember {
            mutableStateOf(
                com.gs.wialonlocal.state.AppSettings(
                    mapType = settings.getMapType(),
                    language = settings.getLanguage(),
                    theme = settings.getTheme()
                )
            )
        }

        val lyricist = rememberStrings()
        ProvideStrings(lyricist) {
            CompositionLocalProvider(
                LocalTheme provides rememberSaveable {
                    mutableStateOf(settings.getTheme() == AppTheme.DARK)
                },
                LocalAppSettings provides remember {
                    mutableStateOf(localSettings.value)
                },
                LocalGlobalNavigator provides remember {
                    mutableStateOf(null)
                }
            ) {
                val theme = LocalTheme.current

                val localSettingsInside = LocalAppSettings.current

                LaunchedEffect(localSettingsInside.value.language) {
                    lyricist.languageTag = localSettingsInside.value.language
                }

                AppTheme(
                    darkTheme = theme.value
                ) {

                    if (versionState.value.loading) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.ytm_green),
                                contentDescription = "logo",
                                modifier = Modifier.size(200.dp)
                            )
                            Spacer(Modifier.height(8.dp))
                            LinearProgressIndicator(Modifier.fillMaxWidth(0.5f))
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Checking version...",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    } else if(versionState.value.error.isNullOrEmpty().not()) {
                        Navigator(GlobalAuthScreen())
                    } else {
                        versionState.value.version?.let { list ->
                            val newVersion =
                                list.find { it.device == getDevice() && it.version != getVersion() }
                            if (newVersion != null && newVersion.required) {
                                UpdateAppComponent(newVersion.version)
                            } else {

                                Navigator(GlobalAuthScreen())
                            }
                        }
                    }
                }


            }
        }
    }
}
