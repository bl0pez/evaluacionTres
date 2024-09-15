package com.photomap.viewModel

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class FormRegistrationViewModel: ViewModel() {
    val formName = mutableStateOf("")
    val photoList = mutableStateOf<List<Uri>>(emptyList())
}