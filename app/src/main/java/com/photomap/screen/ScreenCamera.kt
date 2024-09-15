package com.photomap.screen

import androidx.activity.result.ActivityResultLauncher
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.photomap.Screen
import com.photomap.utils.capturePhoto
import com.photomap.utils.saveImage
import com.photomap.viewModel.AppViewModel
import com.photomap.viewModel.FormRegistrationViewModel

@Composable
fun ScreenCamera(
    permissionLauncher: ActivityResultLauncher<Array<String>>,
    cameraController: LifecycleCameraController
) {

    val context = LocalContext.current
    val formRegisterVM: FormRegistrationViewModel = viewModel()
    val appVM: AppViewModel = viewModel()

    permissionLauncher.launch(arrayOf(android.Manifest.permission.CAMERA))

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                PreviewView(it).apply {
                    controller = cameraController
                }
            }
        )

        Button(
            modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(16.dp),
            onClick = {
                capturePhoto(
                    cameraController,
                    saveImage(context),
                    context
                ) { uri ->
                    formRegisterVM.photoList.value += uri
                    appVM.currentScreenState = Screen.FORM
                }
            }) {
            Text(text = "Capturar Foto")
        }
    }




}