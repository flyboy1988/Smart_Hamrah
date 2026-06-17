package com.smartbank.smarthamrah.features.customers.chat.data.remote

import com.smartbank.smarthamrah.features.customers.chat.data.dto.FileInfoResponseDto
import com.smartbank.smarthamrah.features.customers.chat.data.dto.UploadFileResponseDto
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface FileApi {

    @Multipart
    @POST("api/v1/Files/upload")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part
    ): UploadFileResponseDto

    @GET("api/v1/Files/info/{fileId}")
    suspend fun getFileInfo(
        @Path("fileId") fileId: String
    ): FileInfoResponseDto

    @GET("api/v1/Files/{fileId}")
    suspend fun downloadFile(
        @Path("fileId") fileId: String
    ): ResponseBody
}
