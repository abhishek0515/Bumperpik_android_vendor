package com.bumperpick.bumperpickvendor.API.Provider

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val message: String, val code: Int? = null) : ApiResult<Nothing>()
}

suspend fun <T> safeApiCall(api: suspend () -> Response<T>): ApiResult<T> {
    return try {
        val response = api()
        Log.d("RESPONES",response.body().toString())
        if (response.isSuccessful) {
            response.body()?.let { ApiResult.Success(it) }
                ?: ApiResult.Error("Empty body", response.code())
        } else {
            ApiResult.Error("Error: ${response.message()}", response.code())
        }
    } catch (e: Exception) {
        ApiResult.Error("Exception: ${e.localizedMessage ?: "Unknown error"}")
    }
}
fun File.toMultipartPart(
    partName: String = "file",
    contentType: String = "application/x-www-form-urlencoded"
): MultipartBody.Part {
    val requestBody = this.asRequestBody(contentType.toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(partName, this.name, requestBody)
}

fun String.toPlainTextRequestBody(): RequestBody =
    this.toRequestBody("text/plain".toMediaTypeOrNull())
fun Context.uriToFile(uri: Uri): File? {
    val fileName = getFileName(uri) ?: return null
    val inputStream = contentResolver.openInputStream(uri) ?: return null
    val tempFile = File(cacheDir, fileName)

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
