package com.photomap.viewModel

import android.net.Uri
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class FormRegistrationViewModel: ViewModel() {
    val formName = mutableStateOf("")
    val photoList = mutableStateOf<List<Uri>>(emptyList())
    val latitude = mutableDoubleStateOf(0.0)
    val longitude = mutableDoubleStateOf(0.0)


    fun updateLocation(newLatitude: Double, newLongitude: Double) {
        latitude.doubleValue = newLatitude
        longitude.doubleValue = newLongitude
    }
}