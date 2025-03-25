package org.kotfind.android_course

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

import android.Manifest
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.view.PreviewView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.util.concurrent.Executors
import java.util.concurrent.Executor
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun App() {
    // Column(
    //     modifier = Modifier
    //         .fillMaxSize()
    //         .padding(5.dp)
    // ) {
    //     Text("Hello, world")
    // }
    CameraScreen()
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen() {
    val cameraPermissionState = rememberMultiplePermissionsState(listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    ))

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        // if (!cameraPermissionState.allPermissionsGranted) {
        cameraPermissionState.launchMultiplePermissionRequest()
        // }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        cameraPreview(
            modifier = Modifier.fillMaxSize(),
            lifecycleOwner = lifecycleOwner,
        )

        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp),
            onClick = {
                // TODO
            },
        ) {
            Text("Capture")
        }
    }
}

@Composable
fun cameraPreview(
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner,
) {
    val context = LocalContext.current

    val cameraExecutor = remember {
        Executors.newSingleThreadExecutor()
    }

    val coroutineScope = rememberCoroutineScope()

    AndroidView(
        modifier = modifier,
        factory = { ctx ->

            PreviewView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                scaleType = PreviewView.ScaleType.FILL_CENTER
                
                coroutineScope.launch(Dispatchers.Main) {
                    startCamera(this@apply, context, lifecycleOwner, cameraExecutor)
                }
            }
        }
    )
}

private fun startCamera(
    previewView: PreviewView,
    context: Context,
    lifecycleOwner: LifecycleOwner,
    executor: Executor,
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()

        val preview = androidx.camera.core.Preview.Builder().build()
        preview.setSurfaceProvider(previewView.surfaceProvider)

        try {
            cameraProvider.unbindAll()

            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview
            )
        } catch (e: Exception) {
            Log.e("cameraPreview", "Use case binding failed", e)
        }
    }, executor)
}
