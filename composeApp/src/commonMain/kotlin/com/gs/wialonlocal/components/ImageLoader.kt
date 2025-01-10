package com.gs.wialonlocal.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import wialonlocal.composeapp.generated.resources.Res
import wialonlocal.composeapp.generated.resources.car
import wialonlocal.composeapp.generated.resources.ytm_green
import wialonlocal.composeapp.generated.resources.ytm_white

@Composable
fun ImageLoader(
    modifier: Modifier = Modifier,
    url: String,
    contentScale: ContentScale = ContentScale.Inside
) {
    AsyncImage(
        model = url,
        modifier = modifier,
        contentDescription = null,
        contentScale = contentScale,
        placeholder = painterResource(Res.drawable.ytm_white),
        error = painterResource(Res.drawable.ytm_white)
    )
}