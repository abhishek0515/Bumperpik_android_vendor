package com.bumperpick.bumperpick_Vendor.Misc

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.node.Ref
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

import kotlinx.coroutines.delay
import java.io.File
import java.io.FileOutputStream

fun saveBitmapToInternalImagesDir(context: Context, bitmap: Bitmap): File {
 // Convert hardware bitmap to software-compatible before saving
 val safeBitmap = if (bitmap.config == Bitmap.Config.HARDWARE) {
  bitmap.copy(Bitmap.Config.ARGB_8888, true)
 } else bitmap

 // Target folder inside internal storage for FileProvider
 val imagesDir = File(context.filesDir, "images").apply {
  if (!exists()) mkdirs()
 }

 // Create the image file
 val file = File(imagesDir, "subscription_card_${System.currentTimeMillis()}.png")
 FileOutputStream(file).use { out ->
  safeBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
 }

 return file // Can be used with FileProvider.getUriForFile(...) later
}


@Composable
fun CaptureHost(
 width: Int = 1080,
 onCaptured: (Bitmap) -> Unit,
 content: @Composable () -> Unit
) {
 val context = LocalContext.current
 val viewRef = remember { Ref<ComposeView>() }

 AndroidView(
  factory = {
   ComposeView(context).apply {
    visibility = View.INVISIBLE
    setLayerType(View.LAYER_TYPE_SOFTWARE, null) // Important: disable HW accel
    setContent(content)
    viewRef.value = this
   }
  },
  modifier = Modifier.size(1.dp)
 )

 LaunchedEffect(Unit) {

  delay(100) // Let compose and layout

  viewRef.value?.let { view ->
   view.measure(
    View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
   )
   view.layout(0, 0, view.measuredWidth, view.measuredHeight)

   val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
   val canvas = Canvas(bitmap)
   view.draw(canvas)

   onCaptured(bitmap)
  }
 }
}



