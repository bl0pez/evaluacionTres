package com.photomap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.photomap.screen.ScreenCamera
import com.photomap.screen.ScreenHome
import com.photomap.viewModel.AppViewModel

enum class Screen {
    FORM,
    CAMERA
}


class MainActivity : ComponentActivity() {

    val camaraVM: AppViewModel by viewModels()

    lateinit var  cameraController: LifecycleCameraController

    val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if(it[android.Manifest.permission.CAMERA]?:false) {
          camaraVM.onCameraPermissionGranted()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        cameraController = LifecycleCameraController(this);
        cameraController.bindToLifecycle(this);
        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA


        setContent {
            Scaffold(
                topBar = {
                    TopAppBar(title = { Text("PhotoMapApp") })
                },
                content = { paddingValues ->
                    Column(modifier = Modifier.padding(paddingValues)) {
                        Main(permissionLauncher, cameraController )
                    }
                }
            )
        }
    }
}

@Composable
fun Main(
    permissionLauncher: ActivityResultLauncher<Array<String>>,
    cameraController: LifecycleCameraController
    ) {
    val appVM: AppViewModel = viewModel()

    when (appVM.currentScreenState) {
        Screen.FORM -> ScreenHome()
        Screen.CAMERA -> ScreenCamera(permissionLauncher, cameraController)
    }

}

