package com.photomap

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OnImageSavedCallback
import androidx.camera.core.ImageCapture.OutputFileOptions
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.io.File
import java.time.LocalDateTime

enum class Screen {
    FORM,
    CAMERA
}

class AppVM: ViewModel() {
    var currentScreen by mutableStateOf(Screen.FORM)
    var onPermissionCameraOk: () -> Unit = {}
}

class FormRegisterVM: ViewModel() {
    val name = mutableStateOf("")
    val photos = mutableStateOf<List<Uri>>(emptyList())
}

class MainActivity : ComponentActivity() {

    val camaraVM: AppVM by viewModels()
    val formRegisterVM: FormRegisterVM by viewModels()

    lateinit var  cameraController: LifecycleCameraController

    val lanzadorPermisos = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if(it[android.Manifest.permission.CAMERA]?:false) {
          camaraVM.onPermissionCameraOk()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        cameraController = LifecycleCameraController(this);
        cameraController.bindToLifecycle(this);
        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA


        setContent {
            Main(
                lanzadorPermisos,
                cameraController
            )
        }
    }
}

@Composable
fun Main(
    lanzadorPermisos: ActivityResultLauncher<Array<String>>,
    cameraController: LifecycleCameraController
    ) {
    val appVM: AppVM = viewModel()

    when (appVM.currentScreen) {
        Screen.FORM -> {
            ScreenForm()
        }
        Screen.CAMERA -> {
            ScreenCamera(lanzadorPermisos, cameraController)
        }
    }

}

fun generateNameByDate(): String = LocalDateTime
    .now().toString().replace(Regex("[T:.-]"), "").substring(0, 14)

fun saveImage(context: Context):File = File(
    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
    "${generateNameByDate()}.jpg"
)

fun capturePhoto(
    cameraController: LifecycleCameraController,
    file: File,
    context: Context,
    onImageSave: (uri:Uri) -> Unit
) {

    val options = OutputFileOptions.Builder(file).build()

    cameraController.takePicture(
        options,
        ContextCompat.getMainExecutor(context),
        object : OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                outputFileResults.savedUri?.let {
                    onImageSave(it)
                }
            }

            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(context, "Error al capturar imagen", Toast.LENGTH_SHORT).show()
            }
        }
    )
}

fun uri2imageBitmap(uri:Uri, context: Context) = BitmapFactory.decodeStream(
    context.contentResolver.openInputStream(uri)
).asImageBitmap()

@Composable
fun ScreenForm() {

    val context = LocalContext.current
    val appVm: AppVM = viewModel()
    val formRegisterVM: FormRegisterVM = viewModel()
    var expandedImageUri by remember { mutableStateOf<Uri?>(null) }

    Column() {
        Button(onClick = {
            appVm.currentScreen = Screen.CAMERA
        }) {
            Text(text = "Tomar Foto")
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            content = {
                items(formRegisterVM.photos.value) { uri ->
                    val bitmap = uri2imageBitmap(uri, context)
                    Image(
                        painter = BitmapPainter(bitmap),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
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
            Dialog(onDismissRequest = { expandedImageUri = null }) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                ) {
                    Image(
                        painter = BitmapPainter(uri2imageBitmap(uri, context)),
                        contentDescription = "Imagen ampliada",
                        modifier = Modifier
                            .fillMaxSize()
                            .aspectRatio(4 / 5f) // Ajusta la relación de aspecto para vertical
                            .clickable { expandedImageUri = null } // Oculta la imagen al hacer clic
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ScreenCamera(
    lanzadorPermisos: ActivityResultLauncher<Array<String>>,
    cameraController: LifecycleCameraController
) {

    val context = LocalContext.current
    val formRegisterVM: FormRegisterVM = viewModel()
    val appVM: AppVM = viewModel()

    lanzadorPermisos.launch(arrayOf(android.Manifest.permission.CAMERA))

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
        PreviewView(it).apply {
            controller = cameraController
        }
    })
    Button(onClick = {
        capturePhoto(
            cameraController,
            saveImage(context),
            context
        ) { uri ->
            formRegisterVM.photos.value += uri
            appVM.currentScreen = Screen.FORM

        }
    }) {
        Text(text = "Capturar Foto")
    }
}
