package com.gs.wialonlocal.features.settings.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cafe.adriel.lyricist.strings
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gs.wialonlocal.components.ImageLoader
import com.gs.wialonlocal.components.SwitchText
import com.gs.wialonlocal.core.locale.Locales
import com.gs.wialonlocal.features.auth.data.AuthSettings
import com.gs.wialonlocal.features.auth.presentation.ui.ProfileScreen
import com.gs.wialonlocal.features.main.presentation.ui.ToolBar
import com.gs.wialonlocal.features.monitoring.presentation.ui.settings.InfoTabSettings
import com.gs.wialonlocal.features.monitoring.presentation.ui.settings.MapSettings
import com.gs.wialonlocal.features.monitoring.presentation.ui.settings.MapSource
import com.gs.wialonlocal.features.monitoring.presentation.ui.settings.TitleSettings
import com.gs.wialonlocal.features.monitoring.presentation.ui.settings.UnitViewSettings
import com.gs.wialonlocal.features.settings.data.settings.AppSettings
import com.gs.wialonlocal.features.settings.data.settings.AppTheme
import com.gs.wialonlocal.state.LocalAppSettings
import com.gs.wialonlocal.state.LocalTheme
import org.koin.compose.koinInject

class SettingScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val openTheme = remember {
            mutableStateOf(false)
        }

        val authSettings: AuthSettings = koinInject()

        val openLock = remember {
            mutableStateOf(false)
        }

        val openLanguage = remember {
            mutableStateOf(false)
        }

        val settings = LocalAppSettings.current

        LanguageSelectDialog(
            open = openLanguage.value,
            onDismiss = {
                openLanguage.value = false
            }
        )

        ThemeSelectDialog(
            open = openTheme.value,
            onDismiss = {
                openTheme.value = false
            }
        )

        AutoLockDialog(
            open = openLock.value,
            onDismiss = {
                openLock.value = false
            }
        )

        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).background(MaterialTheme.colorScheme.background)) {
            ToolBar(Modifier) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = strings.settings,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.W500,
                        fontSize = 20.sp
                    )
                )
            }


            Row(
                modifier = Modifier.fillMaxWidth().background(
                    color = MaterialTheme.colorScheme.surface
                ).padding(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clickable {
                        navigator.push(ProfileScreen())
                    }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ImageLoader(
                    modifier = Modifier.size(48.dp)
                        .clip(CircleShape),
                    url = "",
                    contentScale = ContentScale.FillBounds
                )

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = authSettings.getName(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Icon(
                            Icons.AutoMirrored.Default.KeyboardArrowRight,
                            contentDescription = "right",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = strings.tapToChange,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            HorizontalDivider()

            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text(
                    text = strings.interfaceCustom,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.W500,
                        fontSize = 14.sp
                    )
                )
            }

            HorizontalDivider()
            SwitchText(
                modifier = Modifier.fillMaxWidth().clickable {
                    openTheme.value = true
                },
                text = strings.theme,
                subTitle = strings.sameAsDevice,
                arrow = true
            )
            SwitchText(
                modifier = Modifier.fillMaxWidth().clickable {
                    openLanguage.value = true
                },
                text = strings.language,
                subTitle = settings.value.language,
                arrow = true
            )
            SwitchText(
                modifier = Modifier.fillMaxWidth().clickable {
                    navigator.push(MapSource(isBack = false))
                },
                text = strings.mapSettings,
                arrow = true
            )
//            SwitchText(
//                modifier = Modifier.fillMaxWidth().clickable {
//                    navigator.push(NavigationBarSettings())
//                },
//                text = strings.navigationBar,
//                arrow = true
//            )
//            SwitchText(
//                modifier = Modifier.fillMaxWidth().clickable {
//                    navigator.push(UnitViewSettings())
//                },
//                text = strings.unitView,
//                arrow = true
//            )
//            SwitchText(
//                modifier = Modifier.fillMaxWidth().clickable {
//                    navigator.push(InfoTabSettings())
//                },
//                text = strings.infoSettings,
//                arrow = true
//            )

            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text(
                    text = strings.notifications,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.W500,
                        fontSize = 14.sp
                    )
                )
            }

            HorizontalDivider()

            SwitchText(
                modifier = Modifier.fillMaxWidth(),
                text = strings.pushNotification,
                initial = true,
                onChange = {

                }
            )

//            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
//                Text(
//                    text = strings.other,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant,
//                    style = MaterialTheme.typography.bodyLarge.copy(
//                        fontWeight = FontWeight.W500,
//                        fontSize = 14.sp
//                    )
//                )
//            }
//
//            HorizontalDivider()
//
//            SwitchText(
//                modifier = Modifier.fillMaxWidth().clickable {
//                    openTheme.value = true
//                },
//                text = strings.geoFencesAsAddress
//            )

//            SwitchText(
//                modifier = Modifier.fillMaxWidth().clickable {
//                    openLock.value = true
//                },
//                text = strings.autoLock,
//                subTitle = strings.enabled,
//                arrow = true
//            )

            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text(
                    text = strings.help,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.W500,
                        fontSize = 14.sp
                    )
                )
            }

            HorizontalDivider()

            SwitchText(
                modifier = Modifier.fillMaxWidth().clickable {
                    navigator.push(FeedbackScreen())
                },
                text = strings.feedback,
                arrow = true
            )

            Spacer(Modifier.height(12.dp))

        }
    }
}

@Composable
fun ThemeSelectDialog(
    open: Boolean,
    onDismiss: () -> Unit
) {
    if(open) {
        val theme = LocalTheme.current
        val themeType = LocalAppSettings.current
        val settings = koinInject<AppSettings>()
        Dialog(
            onDismissRequest = onDismiss
        ) {
            Column(Modifier.background(MaterialTheme.colorScheme.surface)) {
                Spacer(Modifier.height(16.dp))
                androidx.compose.material.Text(
                    strings.theme,
                    modifier = Modifier.padding(start = 16.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.W500
                    )
                )
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    RadioButton(
                        selected = themeType.value.theme == AppTheme.LIGHT,
                        onClick = {
                            theme.value = false
                            themeType.value = themeType.value.copy(
                                theme = AppTheme.LIGHT
                            )
                            settings.saveTheme("light")
                        }
                    )

                    androidx.compose.material.Text(
                        strings.light,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.W500
                        )
                    )
                }
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    RadioButton(
                        selected = themeType.value.theme == AppTheme.DARK,
                        onClick = {
                            theme.value = true
                            themeType.value = themeType.value.copy(
                                theme = AppTheme.DARK
                            )
                            settings.saveTheme("dark")
                        }
                    )

                    androidx.compose.material.Text(
                        strings.dark,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.W500
                        )
                    )
                }
                Spacer(Modifier.height(6.dp))
                HorizontalDivider()
                Spacer(Modifier.height(6.dp))
                Row(
                    Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)
                        .padding(12.dp), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {

                    androidx.compose.material.Text(
                        modifier = Modifier.clickable {
                            onDismiss()
                        },
                        text = strings.cancel,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W500),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.width(12.dp))

                    androidx.compose.material.Text(
                        modifier = Modifier.clickable {
                            onDismiss()
                        },
                        text = "OK",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W500),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}


@Composable
fun AutoLockDialog(
    open: Boolean,
    onDismiss: () -> Unit
) {
    if(open) {
        val theme = LocalTheme.current
        Dialog(
            onDismissRequest = onDismiss
        ) {
            Column(Modifier.background(MaterialTheme.colorScheme.surface)) {
                Spacer(Modifier.height(16.dp))
                androidx.compose.material.Text(
                    strings.autoLock,
                    modifier = Modifier.padding(start = 16.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.W500
                    )
                )
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    RadioButton(
                        selected = false,
                        onClick = {
                            theme.value = false
                        }
                    )

                    androidx.compose.material.Text(
                        strings.enabled,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.W500
                        )
                    )
                }
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    RadioButton(
                        selected = true,
                        onClick = {
                            theme.value = false
                        }
                    )

                    androidx.compose.material.Text(
                        strings.disabled,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.W500
                        )
                    )
                }
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    RadioButton(
                        selected = true,
                        onClick = {
                            theme.value = true
                        }
                    )

                    androidx.compose.material.Text(
                        strings.disabledWhileCharging,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.W500
                        )
                    )
                }
                Spacer(Modifier.height(6.dp))
                HorizontalDivider()
                Spacer(Modifier.height(6.dp))
                Row(
                    Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)
                        .padding(12.dp), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {

                    androidx.compose.material.Text(
                        modifier = Modifier.clickable {
                            onDismiss()
                        },
                        text = strings.cancel,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W500),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.width(12.dp))

                    androidx.compose.material.Text(
                        modifier = Modifier.clickable {
                            onDismiss()
                        },
                        text = "OK",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W500),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}


@Composable
fun LanguageSelectDialog(
    open: Boolean,
    onDismiss: () -> Unit
) {
    if(open) {
        val language = LocalAppSettings.current
        val settings = koinInject<AppSettings>()
        Dialog(
            onDismissRequest = onDismiss
        ) {
            Column(Modifier.background(MaterialTheme.colorScheme.surface)) {
                Spacer(Modifier.height(16.dp))
                androidx.compose.material.Text(
                    strings.language,
                    modifier = Modifier.padding(start = 16.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.W500
                    )
                )
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    RadioButton(
                        selected = language.value.language == Locales.TM,
                        onClick = {
                            language.value = language.value.copy(
                                language = Locales.TM
                            )
                            settings.saveLanguage(Locales.TM)
                        }
                    )

                    androidx.compose.material.Text(
                        "Turkmen",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.W500
                        )
                    )
                }
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    RadioButton(
                        selected = language.value.language == Locales.RU,
                        onClick = {
                            language.value = language.value.copy(
                                language = Locales.RU
                            )
                            settings.saveLanguage(Locales.RU)
                        }
                    )

                    androidx.compose.material.Text(
                        "Russian",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.W500
                        )
                    )
                }
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    RadioButton(
                        selected = language.value.language == Locales.EN,
                        onClick = {
                            language.value = language.value.copy(
                                language = Locales.EN
                            )
                            settings.saveLanguage(Locales.EN)
                        }
                    )

                    androidx.compose.material.Text(
                        "English",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.W500
                        )
                    )
                }
                Spacer(Modifier.height(6.dp))
                HorizontalDivider()
                Spacer(Modifier.height(6.dp))
                Row(
                    Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)
                        .padding(12.dp), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {

                    androidx.compose.material.Text(
                        modifier = Modifier.clickable {
                            onDismiss()
                        },
                        text = strings.cancel,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W500),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.width(12.dp))

                    androidx.compose.material.Text(
                        modifier = Modifier.clickable {
                            onDismiss()
                        },
                        text = "OK",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W500),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}