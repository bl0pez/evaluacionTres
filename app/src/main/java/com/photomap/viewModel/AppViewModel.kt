package com.photomap.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.photomap.Screen

class AppViewModel: ViewModel() {
    var currentScreenState by mutableStateOf(Screen.FORM)
    var onCameraPermissionGranted: () -> Unit = {}
}