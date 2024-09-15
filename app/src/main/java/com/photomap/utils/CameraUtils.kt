package com.photomap.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OnImageSavedCallback
import androidx.camera.core.ImageCapture.OutputFileOptions
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.ContextCompat
import java.io.File
import java.time.LocalDateTime

fun generateNameByDate(): String = LocalDateTime.now().toString().replace(Regex("[T:.-]"), "").substring(0, 14)

fun uriToImageBitmap(uri:Uri, context: Context) = BitmapFactory.decodeStream(
    context.contentResolver.openInputStream(uri)
).asImageBitmap()

fun saveImage(context: Context): File = File(
    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
    "${generateNameByDate()}.jpg"
)

fun capturePhoto(
    cameraController: LifecycleCameraController,
    file: File,
    context: Context,
    onImageSave: (uri: Uri) -> Unit
) {
    val options = OutputFileOptions.Builder(file).build()

    cameraController.takePicture(
        options,
        ContextCompat.getMainExecutor(context),
        object : OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                outputFileResults.savedUri?.let(onImageSave)
            }

            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(context, "Error al capturar imagen", Toast.LENGTH_SHORT).show()
            }
        }
    )
}