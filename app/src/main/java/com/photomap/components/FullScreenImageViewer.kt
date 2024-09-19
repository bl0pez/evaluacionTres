package com.photomap.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.photomap.Screen
import com.photomap.utils.uriToImageBitmap
import com.photomap.viewModel.AppViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun FullScreenImageViewer(
    uri: Uri,
    onDismiss: () -> Unit
) {

    val context = LocalContext.current
    val appVm: AppViewModel = viewModel()

    Dialog(onDismissRequest = { onDismiss() }) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = BitmapPainter(uriToImageBitmap(uri, context)),
                    contentDescription = "Imagen ampliada",
                    modifier = Modifier
                        .clickable { onDismiss() }
                        .fillMaxSize()
                )
                Button(
                    onClick = {
                        appVm.currentScreenState = Screen.MAP
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                ) {
                    Text(text = "Ver Ubicación")
                }
            }
    }
}