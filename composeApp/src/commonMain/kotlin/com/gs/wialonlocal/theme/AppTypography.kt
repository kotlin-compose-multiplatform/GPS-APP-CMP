package com.gs.wialonlocal.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import wialonlocal.composeapp.generated.resources.Res
import wialonlocal.composeapp.generated.resources.bold
import wialonlocal.composeapp.generated.resources.extrabold
import wialonlocal.composeapp.generated.resources.extralight
import wialonlocal.composeapp.generated.resources.light
import wialonlocal.composeapp.generated.resources.medium
import wialonlocal.composeapp.generated.resources.regular
import wialonlocal.composeapp.generated.resources.semibold

@Composable
fun PoppinsFontFamily() = FontFamily(
    Font(Res.font.light, weight = FontWeight.Light),
    Font(Res.font.regular, weight = FontWeight.Normal),
    Font(Res.font.medium, weight = FontWeight.Medium),
    Font(Res.font.semibold, weight = FontWeight.SemiBold),
    Font(Res.font.bold, weight = FontWeight.Bold),
    Font(Res.font.extrabold, weight = FontWeight.ExtraBold),
    Font(Res.font.extralight, weight = FontWeight.ExtraLight),
)