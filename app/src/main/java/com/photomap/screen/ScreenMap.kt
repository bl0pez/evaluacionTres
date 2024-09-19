package com.photomap.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.photomap.Screen
import com.photomap.viewModel.AppViewModel
import com.photomap.viewModel.FormRegistrationViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun ScreenMap() {

    val formRegisterVM: FormRegistrationViewModel = viewModel()

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = {
                MapView(it).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    org.osmdroid.config.Configuration.getInstance().userAgentValue = context.packageName
                    controller.setZoom(15.0)
                }
            },
            update = {
                it.overlays.removeIf { true }
                it.invalidate()

                val geoPoint = GeoPoint(formRegisterVM.latitude.doubleValue, formRegisterVM.longitude.doubleValue)
                it.controller.animateTo(geoPoint)

                val marcador = Marker(it)
                marcador.position = geoPoint
                marcador.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                it.overlays.add(marcador)
            }
        )

        BackButton()
    }
}

@Composable
fun BackButton() {

    val appVM: AppViewModel = viewModel()

    FloatingActionButton(
        onClick = {
            appVM.currentScreenState = Screen.FORM
        },
        shape = CircleShape,

    ) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Floating action button.")
    }
}