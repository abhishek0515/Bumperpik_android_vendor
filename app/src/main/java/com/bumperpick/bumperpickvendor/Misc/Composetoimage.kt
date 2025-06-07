package com.bumperpick.bumperpickvendor.Misc

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.node.Ref
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import com.bumperpick.bumperpickvendor.Repository.Feature
import com.bumperpick.bumperpickvendor.Repository.FeatureType
import com.bumperpick.bumperpickvendor.Repository.Plan
import com.bumperpick.bumperpickvendor.Screens.Subscription.SubscriptionCard
import com.bumperpick.bumperpickvendor.Screens.Subscription.SubscriptionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
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



