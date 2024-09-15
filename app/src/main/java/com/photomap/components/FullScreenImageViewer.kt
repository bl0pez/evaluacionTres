package com.photomap.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.photomap.utils.uriToImageBitmap

@Composable
fun FullScreenImageViewer(uri: Uri, onDismiss: () -> Unit) {

    val context = LocalContext.current

    Dialog(onDismissRequest = { onDismiss() }) {
            Image(
                painter = BitmapPainter(uriToImageBitmap(uri, context)),
                contentDescription = "Imagen ampliada",
                modifier = Modifier
                    .clickable { onDismiss() }
                    .fillMaxSize()
            )
    }
}