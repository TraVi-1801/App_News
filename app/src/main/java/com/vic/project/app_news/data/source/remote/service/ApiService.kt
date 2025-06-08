package com.vic.project.app_news.data.source.remote.service

import com.vic.project.app_news.data.source.remote.model.response.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.HeaderMap
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface ApiService {

    @GET
    suspend fun get(@HeaderMap headers: Map<String, String>, @Url url: String): BaseResponse

    @GET
    suspend fun get(
        @HeaderMap headers: Map<String, String>,
        @Url url: String,
        @QueryMap params: Map<String, String>
    ): BaseResponse

    @POST
    suspend fun post(
        @HeaderMap headers: Map<String, String>,
        @Url url: String,
        @Body o: Any?
    ): BaseResponse

    @PUT
    suspend fun put(
        @HeaderMap headers: Map<String, String>,
        @Url url: String,
        @Body o: Any?
    ): BaseResponse

    @HTTP(method = "DELETE", hasBody = true)
    suspend fun delete(
        @HeaderMap headers: Map<String, String>,
        @Url url: String,
        @Body o: Any?
    ): BaseResponse

    @Multipart
    @POST
    suspend fun uploadFile(
        @HeaderMap headers: Map<String, String>,
        @Url url: String,
        @Part file: MultipartBody.Part?,
        @Part("type") type: RequestBody?
    ): BaseResponse

    @Multipart
    @PUT
    suspend fun savePhotoOfParcel(
        @HeaderMap headers: Map<String, String>,
        @Url url: String,
        @Part file: MultipartBody.Part?,
        @Part("orderId") orderId: RequestBody?,
        @Part("type") type: RequestBody?,
    ): BaseResponse

    @Multipart
    @PUT
    suspend fun uploadFileKYC(
        @HeaderMap headers: Map<String, String>,
        @Url url: String,
        @Part file: MultipartBody.Part?,
        @Part("type") type: RequestBody?
    ): BaseResponse

    @Multipart
    @POST
    suspend fun uploadMultiFile(
        @HeaderMap headers: Map<String, String>,
        @Url url: String,
        @Part file: Array<MultipartBody.Part?>,
        @Part("type") type: RequestBody?
    ): BaseResponse

    @POST
    suspend fun uploadBinaryFile(
        @HeaderMap headers: Map<String, String>,
        @Url url: String,
        @Body file: RequestBody
    ) : BaseResponse
}