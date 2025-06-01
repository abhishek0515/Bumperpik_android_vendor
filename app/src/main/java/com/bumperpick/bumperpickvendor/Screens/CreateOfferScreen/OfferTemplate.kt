package com.bumperpick.bumperpickvendor.Screens.CreateOfferScreen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RadialGradient
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bumperpick.bumperpickvendor.R
import com.bumperpick.bumperpickvendor.Repository.TextType
import com.bumperpick.bumperpickvendor.Screens.Component.TextFieldView

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import com.bumperpick.bumperpickvendor.Repository.Template_Data








fun getGradientByColorType(type: ColorType,): List<Color> {
    return when (type) {
        ColorType.BLUE -> RadialGradientType.BLUE()
        ColorType.RED -> RadialGradientType.RED()
        ColorType.PINK -> RadialGradientType.PINK()
        ColorType.PURPLE -> RadialGradientType.PURPLE()
        ColorType.ORANGE -> RadialGradientType.ORANGE()
    }
}
interface GradientList{
    fun BLUE():List<Color>
    fun RED():List<Color>
    fun PINK():List<Color>
    fun PURPLE():List<Color>
    fun ORANGE():List<Color>
}
enum class ColorType{
    BLUE,RED,PINK,PURPLE,ORANGE
}
object RadialGradientType : GradientList {

    private val BLUE = listOf(
        Color(0xFFE6F3FF), // Very Light Blue
        Color(0xFFB6E0FF), // Light Sky Blue
        Color(0xFF87CEEB), // Sky Blue (Darker)
        Color(0xFF4682B4)  // Steel Blue (Darkest)
    )

    private val RED = listOf(
        Color(0xFFFFE3E5), // Very Light Red
        Color(0xFFFF9999), // Light Coral
        Color(0xFFFF6B6B), // Coral Red
        Color(0xFFB22222)  // Firebrick
    )

    private val PINK = listOf(
        Color(0xFFFFDEFC), // Very Light Pink
        Color(0xFFFF99CC), // Light Pink
        Color(0xFFFF69B4), // Hot Pink
        Color(0xFFDB3E79)  // Deep Pink
    )

    private val PURPLE = listOf(
        Color(0xFFD8C7FA), // Soft Lavender
        Color(0xFFB89EF8), // Light Purple
        Color(0xFF732EFD), // Medium Purple
        Color(0xFF681CFA)  // Dark Purple
    )

    private val ORANGE = listOf(
        Color(0xFFFFE5D8), // Very Light Orange
        Color(0xFFFFA366), // Medium Orange
        Color(0xFFFF8C42), // Deep Orange
        Color(0xFFCC5500)  // Burnt Orange
    )

    override fun BLUE(): List<Color> = BLUE
    override fun RED(): List<Color> = RED
    override fun PINK(): List<Color> = PINK
    override fun PURPLE(): List<Color> = PURPLE
    override fun ORANGE(): List<Color> = ORANGE
}





@Composable
fun Template1(
    data: Template_Data,
    modifier: Modifier = Modifier
) {
    val gradient = getGradientByColorType(data.gradientType)

    RadialGradientCard  (
        gradientList = gradient,
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {

            // Main Content
            ContentSection(
                data = data,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.75f) // Use 75% width to avoid image overlap
                    .align(Alignment.CenterStart)
            )

            // Decorative Image
            DecorativeImage(
                image = R.drawable.template_image1,
                modifier = Modifier
                    .size(400.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x=140.dp,y=15.dp)

            )
        }
    }
}

@Composable
fun Template2(
    data: Template_Data,
    modifier: Modifier = Modifier
) {
    val gradient = getGradientByColorType(data.gradientType)

    LinearGradientCard (
        gradientColors = gradient,
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {

            // Main Content
            ContentSection(
                data = data,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.75f) // Use 75% width to avoid image overlap
                    .align(Alignment.CenterStart)
            )

            // Decorative Image
            DecorativeImage(
                image = R.drawable.template2,
                modifier = Modifier
                    .size(400.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x=140.dp,y=15.dp)

            )
        }
    }
}

@Composable
fun Template3(
    data: Template_Data,
    modifier: Modifier = Modifier
) {
    val gradient = getGradientByColorType(data.gradientType)
    LinearGradientCard (
        gradientColors = gradient,
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {

            // Main Content
            ContentSection(
                data = data,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.75f) // Use 75% width to avoid image overlap
                    .align(Alignment.CenterStart)
            )

            // Decorative Image
            DecorativeImage(
                image = R.drawable.template3,
                modifier = Modifier
                    .size(400.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x=118.dp,y=15.dp)

            )
        }
    }
}

@Composable
fun Template4(
    data: Template_Data,
    modifier: Modifier = Modifier
) {
    val gradient = getGradientByColorType(data.gradientType)

    LinearGradientCard (
        gradientColors = gradient,
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {

            // Main Content
            ContentSection(
                data = data,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.75f) // Use 75% width to avoid image overlap
                    .align(Alignment.CenterStart)
            )

            // Decorative Image
            DecorativeImage(
                image = R.drawable.template4,
                modifier = Modifier
                    .size(400.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x=120.dp,y=20.dp)

            )
        }
    }
}

@Composable
private fun ContentSection(
    data: Template_Data,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        // Brand Section
        BrandSection(
            logo = data.Logo,
            brandName = data.brandName,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Heading
        Text(
            text = data.heading.text,

            fontSize =  with(LocalDensity.current) { data.heading.fontSize.value.toSp() },
            fontWeight =if(data.heading.bold) FontWeight.Bold else FontWeight.Normal,
            fontStyle = if(data.heading.italic) FontStyle.Italic else FontStyle.Normal,

            color = Color.Black,
            lineHeight = 26.sp,
            modifier = Modifier.padding(bottom = 6.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        // Subheading
        Text(
            text = data.subHeading.text,
            fontSize = 12.sp,
            fontWeight =if(data.subHeading.bold) FontWeight.Bold else FontWeight.Normal,
            fontStyle = if(data.subHeading.italic) FontStyle.Italic else FontStyle.Normal,
            color = Color.Black.copy(alpha = 0.5f),
            lineHeight = 20.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}
fun Bitmap.toSoftwareBitmap(): Bitmap {
    return if (this.config == Bitmap.Config.HARDWARE) {
        this.copy(Bitmap.Config.ARGB_8888, true)
    } else this
}
fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        if (Build.VERSION.SDK_INT < 28) {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri).toSoftwareBitmap()
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source).toSoftwareBitmap()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
@Composable
private fun BrandSection(
    logo: Uri?,
    brandName: TextType,
    modifier: Modifier = Modifier
) {   val context= LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(logo) {
        bitmap = logo?.let { uriToBitmap(context, it) }
    }
    if (logo != null) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            bitmap?.let {
                Image(
                    bitmap= it.asImageBitmap(),
                    contentDescription = "Brand Logo",
                    modifier = Modifier
                        .width(200.dp)
                        .height(50.dp)
                        .background(Color.Transparent),
                    alignment = Alignment.TopStart,
                    contentScale = ContentScale.Fit,

                    )
            }
        }
    } else {
        Text(
            text = brandName.text,
            fontSize = 18.sp,
            fontWeight =if(brandName.bold) FontWeight.Bold else FontWeight.Normal,
            fontStyle = if(brandName.italic) FontStyle.Italic else FontStyle.Normal,
            color = Color.Black,
            modifier = modifier,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun DecorativeImage(
    modifier: Modifier = Modifier,
    image:Int
) {
    Image(
        painter = painterResource(image),
        contentDescription = null, // Decorative image
        modifier = modifier.fillMaxHeight(),
        contentScale = ContentScale.FillHeight,


    )
}


@Composable
fun RadialGradientCard(modifier: Modifier = Modifier,
                       gradientList: List<Color>,
                       content: @Composable () -> Unit={} ) {

        val gc=gradientList
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = modifier,
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            border = BorderStroke(1.dp,gradientList.get(0)),

        ) {
            Box(modifier = Modifier.fillMaxSize() .background(
                brush = Brush.verticalGradient(
                    colors = gc,


                    )
            ).padding(16.dp)){
            Box(modifier = Modifier
                    .align(alignment = Alignment.CenterEnd)
                    .requiredSize(size = 340.dp)
                    .scale(1.2f)
                    .offset(x=120.dp)
                    .clip(shape = CircleShape)
                    .background(color =gradientList[2]))
            {
                Box(
                    modifier = Modifier
                        .align(alignment =Alignment.CenterEnd )
                        .offset(x = 0.dp,
                        )
                        .requiredSize(size = 300.dp)
                        .clip(shape = CircleShape)
                        .background(color = gradientList[1])){
                    Box(
                        modifier = Modifier
                            .align(alignment = Alignment.CenterEnd)
                            .offset(x = 0.dp,
                                y = 0.dp)
                            .requiredSize(size =260.dp)
                            .clip(shape = CircleShape)
                            .background(color = gradientList[2]))
                }
            }
                content()
        }
    }
}



@Composable
fun LinearGradientCard(
    gradientColors: List<Color>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val gc=gradientColors
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp,gradientColors.get(0))
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = gc,


                    )
                )
                .padding(16.dp)
        ) {
            content()
        }
    }
}







