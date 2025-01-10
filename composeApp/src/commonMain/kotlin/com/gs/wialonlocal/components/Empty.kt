package com.gs.wialonlocal.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.lyricist.LocalStrings
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import wialonlocal.composeapp.generated.resources.Res

@OptIn(ExperimentalResourceApi::class)
@Preview
@Composable
fun Empty(
    modifier: Modifier = Modifier,
    text: String = LocalStrings.current.noData,
    imageModifier: Modifier = Modifier.width(150.dp).height(130.dp)
) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/empty.json").decodeToString()
        )
    }
    Column(
        modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberLottiePainter(
                composition = composition,
                iterations = Compottie.IterateForever
            ),
            contentDescription = "empty",
            modifier = imageModifier
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.W700,
                fontSize = 18.sp
            ),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun Empty(
    modifier: Modifier = Modifier,
    image: Painter,
    text: String,
    imageModifier: Modifier = Modifier.width(150.dp).height(130.dp)
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = image,
            contentDescription = "empty",
            modifier = imageModifier
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.W700,
                fontSize = 18.sp
            ),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}