package com.bumperpick.bumperickUser.Screens.Faq

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumperpick.bumperpick_Vendor.API.FinalModel.faq
import com.bumperpick.bumperpick_Vendor.Screens.QrScreen.UiState
import com.bumperpick.bumperpick_Vendor.ui.theme.BtnColor


import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.compose.koinViewModel

@Preview
@Composable
fun preview_faq(){

}

@Composable
fun faq(onBackClick:()->Unit,
        viewmodel: FaqViewmodel= koinViewModel()
) {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color(0xFF5A0E26) // Your desired color
    val faqs=viewmodel.faq_uistate.collectAsState().value
    LaunchedEffect(Unit) {
        viewmodel.loadFaq()
    }
    // Change status bar color
    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false // true for dark icons on light background
        )
    }
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color(0xFFFAFAFA))
        )
        {
            var size by remember { mutableStateOf(IntSize.Zero) }
            val backgroundModifier = remember(size) {
                if (size.width > 0 && size.height > 0) {
                    val radius = kotlin.comparisons.maxOf(size.width, size.height) / 1.5f
                    Modifier.background(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFF8B1538), Color(0xFF5A0E26)),
                            center = Offset(size.width / 2f, size.height / 2f),
                            radius = radius
                        )
                    )
                } else {
                    Modifier.background(Color(0xFF8B1538))
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged { size = it },
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 24.dp,
                    bottomEnd = 24.dp
                ),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            )
            {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(backgroundModifier)
                        .padding(bottom = 0.dp)
                ) {
                    Spacer(modifier = Modifier.height(12.dp))

                    // Top App Bar with improved spacing
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                    {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            IconButton(
                                onClick = onBackClick,
                                modifier = Modifier.size(44.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Frequently Asked Question",
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = Color.White
                            )
                        }


                    }

                    Spacer(modifier = Modifier.height(12.dp))

                }


            }
            Spacer(modifier = Modifier.height(12.dp))

            when(faqs){

                UiState.Empty -> {

                }
                is UiState.Error ->{
                    Box(modifier = Modifier.fillMaxSize()){
                        Text(text = faqs.message,
                            modifier = Modifier.align(Alignment.Center).padding(20.dp),
                            color = BtnColor)
                    }
                }
                UiState.Loading ->{
                    Box(modifier = Modifier.fillMaxSize()){
                        CircularProgressIndicator(color = BtnColor, modifier = Modifier.align(
                            Alignment.Center))
                    }
                }
                is UiState.Success -> {
                    val data=faqs.data.map { it.toEntity() }
                    FaqScreen(data)
                }
            }







        }

    }
}
data class FaqItem(val question: String, val answer: String)
fun faq.toEntity(): FaqItem=
    FaqItem(
        question=this.question,
        answer=this.answer)






@Composable
fun FaqScreen(faqList: List<FaqItem>) {
    LazyColumn {
        items(faqList) { faq ->
            FaqCard(question = faq.question, answer = faq.answer)
        }
    }
}
@Composable
fun ExpandCollapseButton(isExpanded: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(BtnColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = if(isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.White, modifier = Modifier.align(
            Alignment.Center))
    }
}

@Composable
fun FaqCard(question: String, answer: String) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(modifier = Modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = question, style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                )
                ExpandCollapseButton(isExpanded = isExpanded, onClick = { isExpanded = !isExpanded })
            }
            if (isExpanded) {
                Text(text = answer, modifier = Modifier.padding(top = 8.dp), color = Color.Gray, fontSize = 14.sp)
            }
        }
    }
}