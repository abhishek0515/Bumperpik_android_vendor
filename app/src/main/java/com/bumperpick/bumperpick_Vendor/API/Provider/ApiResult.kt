package com.bumperpick.bumperpick_Vendor.API.Provider

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.bumperpick.bumperpick_Vendor.API.Model.DataXXXXX
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import kotlin.text.contains

sealed class ApiResult<out T, out E> {
    data class Success<out T>(val data: T) : ApiResult<T, Nothing>()
    data class Error<out E>(val error: E, val code: Int? = null) : ApiResult<Nothing, E>()
}


suspend fun <T, E> safeApiCall(
    api: suspend () -> Response<T>,
    errorBodyParser: (String) -> E
): ApiResult<T, E> {
    return try {
        val response = api()
        Log.d("RESPONSE", response.body().toString())
        if (response.isSuccessful) {
            response.body()?.let { ApiResult.Success(it) }
                ?: ApiResult.Error(errorBodyParser("Empty body"), response.code())
        } else {
            val errorBody = response.errorBody()?.string().orEmpty()
            val parsedError = errorBodyParser(errorBody)
            Log.d("Error", errorBody)
            if(errorBody.contains("Unauthenticated", true) || response.code()==401){

            }
            ApiResult.Error(parsedError, response.code())
        }
    } catch (e: Exception) {
        Log.d("Exception", e.localizedMessage ?: "Unknown error")
        ApiResult.Error(errorBodyParser(e.localizedMessage ?: "Unknown error"))
    }
}

fun prepareImageParts(images: List<File>): List<MultipartBody.Part> {
    return images.mapIndexed { index, file ->
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        MultipartBody.Part.createFormData("media[]", file.name, requestFile)
    }
}
fun File.toMultipartPart(partName: String): MultipartBody.Part {
    val mediaType = when (extension.lowercase()) {
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "pdf" -> "application/pdf"
        else -> "application/octet-stream"
    }.toMediaTypeOrNull()

    val requestFile = this.asRequestBody(mediaType)
    return MultipartBody.Part.createFormData(partName, name, requestFile)
}

fun String.toPlainTextRequestBody(): RequestBody =
    this.toRequestBody("text/plain".toMediaTypeOrNull())
fun Context.uriToFile(uri: Uri): File? {
    val fileName = getFileName(uri) ?: return null
    val inputStream = contentResolver.openInputStream(uri) ?: return null
    val tempFile = File(cacheDir, fileName)
    Log.d("Offer banner",inputStream.toString())

    tempFile.outputStream().use { output ->
        inputStream.copyTo(output)
    }

    return tempFile
}

fun Context.getFileName(uri: Uri): String? {
    var name: String? = null
    if (uri.scheme == "content") {
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (idx >= 0) name = cursor.getString(idx)
            }
        }
    }
    if (name == null) {
        name = uri.path?.substringAfterLast('/')
    }
    return name
}
