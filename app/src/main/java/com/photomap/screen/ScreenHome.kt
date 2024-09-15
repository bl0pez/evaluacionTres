package com.photomap.screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.photomap.Screen
import com.photomap.components.FullScreenImageViewer
import com.photomap.utils.uriToImageBitmap
import com.photomap.viewModel.AppViewModel
import com.photomap.viewModel.FormRegistrationViewModel

@Composable
fun ScreenHome() {

    val context = LocalContext.current
    var expandedImageUri by remember { mutableStateOf<Uri?>(null) }
    val appVm: AppViewModel = viewModel()
    val formVm : FormRegistrationViewModel = viewModel()
    
    Column {
        Button(onClick = {
            appVm.currentScreenState = Screen.CAMERA
        }) {
            Text(text = "Tomar Foto")
        }
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            content = {
                items(formVm.photoList.value) { uri ->
                    Image(
                        painter = BitmapPainter(uriToImageBitmap(uri, context)),
                        contentDescription = "Photo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .clickable {
                                expandedImageUri = uri
                            }
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        expandedImageUri?.let { uri ->
            FullScreenImageViewer(uri = uri, onDismiss = { expandedImageUri = null })
        }
    }


}