package com.bumperpick.bumperpickvendor.Screens.Component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.BasicTextField

import androidx.compose.ui.draw.drawWithContent

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.Stroke

import androidx.compose.ui.text.input.TextFieldValue

import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bumperpick.bumperpickvendor.R
import com.bumperpick.bumperpickvendor.ui.theme.BtnColor
import com.bumperpick.bumperpickvendor.ui.theme.satoshi
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_regular

import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.bumperpick.bumperpickvendor.ui.theme.grey
import com.bumperpick.bumperpickvendor.ui.theme.satoshi_medium

/**
 * Primary action button with consistent styling
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = BtnColor
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontFamily = satoshi_medium
        )
    }
}

/**
 * Secondary action button with consistent styling
 */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = enabled,
        border = BorderStroke(0.dp, Color.Transparent),

        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = BtnColor,
            containerColor = BtnColor.copy(alpha = 0.1f),
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontFamily = satoshi_medium

        )
    }
}
@Composable
fun TextFieldView(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Placeholder...",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textStyle: TextStyle = LocalTextStyle.current,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White, // Default light gray background
    singleLine: Boolean = true,
    isEnabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                style = textStyle.copy(color = Color.Gray)
            )
        },
        enabled = isEnabled,
        keyboardOptions = keyboardOptions,
        textStyle = textStyle,
        modifier = modifier,
        singleLine = singleLine,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = containerColor,

            focusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
            cursorColor =  BtnColor,
            focusedBorderColor =  BtnColor,
            unfocusedBorderColor = Color.Gray,

        )
    )
}

@Composable
fun ButtonView(text:String,btnColor:Color= BtnColor,modifier: Modifier=Modifier,horizontal_padding:Dp=16.dp,onClick:()->Unit,) {
    Button(
        onClick = { onClick() },
        modifier = modifier

            .fillMaxWidth()
            .height(75.dp)
            .padding( bottom = 20.dp, start =  horizontal_padding, end = horizontal_padding),

        colors = ButtonDefaults.buttonColors(
            containerColor = btnColor
        ),
        shape = RoundedCornerShape(16.dp)

    ) {
        Text(text, color = Color.White, fontFamily = satoshi_regular, fontWeight = FontWeight.Bold, fontSize = 18.sp)
    }
}

@Composable
fun Google_SigInButton(modifier: Modifier=Modifier,onCLick:()->Unit) {


    OutlinedButton(
        onClick = { onCLick()},
        border = BorderStroke(0.dp, Color.Transparent),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(),
        modifier = modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .background(
                color = Color(0xFFF0F0F0),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 16.dp,)
        ) {
            Image(
                painter = painterResource(R.drawable.google_icon_logo_svgrepo_com),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(end = 10.dp,)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .width(18.dp)
                    .height(18.dp)
            )
            Text(
                "Sign in with Google",
                color = Color(0xFF212427),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
@Composable
fun OtpView(
    numberOfOtp: Int,
    value: String,
    onValueChange: (String) -> Unit,
    otpCompleted: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequesters = List(numberOfOtp) { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(numberOfOtp) { index ->
            val char = if (index < value.length) value[index].toString() else ""
            OutlinedTextField(
                value = char,
                onValueChange = { newChar ->
                    if (newChar.length <= 1) {
                        val newValue = buildString {
                            append(value.take(index))
                            append(newChar)
                            append(value.drop(index + 1))
                        }.take(numberOfOtp)
                        onValueChange(newValue)
                        if (newChar.isNotEmpty() && index < numberOfOtp - 1) {
                            focusRequesters[index + 1].requestFocus()
                        } else if (newChar.isEmpty() && index > 0) {
                            focusRequesters[index - 1].requestFocus()
                        }
                        if (newValue.length == numberOfOtp) {
                            otpCompleted(newValue)
                            keyboardController?.hide()
                        }
                    }
                },
                modifier = Modifier
                    .width(48.dp)
                    .focusRequester(focusRequesters[index]),
                textStyle = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BtnColor,
                    unfocusedBorderColor = Color.Gray,
                    disabledBorderColor = Color.Black,
                    focusedContainerColor = grey,
                    unfocusedContainerColor = grey,
                    disabledContainerColor = grey,
                    cursorColor = BtnColor
                ),
                shape = RoundedCornerShape(10.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        if (index < numberOfOtp - 1) {
                            focusRequesters[index + 1].requestFocus()
                        }
                    }
                ),
                singleLine = true
            )
        }
    }

    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }
}






