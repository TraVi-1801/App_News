package com.vic.project.app_news.data.source.remote.service

import com.vic.project.app_news.data.source.remote.model.response.BaseResponse
import com.vic.project.app_news.data.source.remote.model.response.ResultWrapper
import com.vic.project.app_news.di.AppModule
import com.vic.project.app_news.utils.AppConstants
import com.vic_project.search_image.data.remote.models.request.KeyRequest
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RetrofitService @Inject constructor(
    @AppModule.MainInterceptor private val apiService: ApiService,
    @AppModule.OtherInterceptor private val apiServiceFile: ApiService,
) {
    companion object {
        const val TIME_OUT = 30000L
        const val LONG_TIME_OUT = 60000L
    }

    suspend fun getMethod(
        headers: Map<String, String>,
        request: KeyRequest,
        message: Any? = null,
        codeRequired: String
    ): ResultWrapper<BaseResponse> {
        return safeApiCall({
            apiService.get(headers, request.url + message)
        }, codeRequired)
    }

    suspend fun getMethod(
        headers: Map<String, String>,
        request: KeyRequest,
        params: Map<String, String>,
        codeRequired: String
    ): ResultWrapper<BaseResponse> {
        return safeApiCall({
            apiService.get(headers, request.url, params)
        }, codeRequired)
    }

    suspend fun postMethod(
        headers: Map<String, String>,
        request: KeyRequest,
        message: Any? = null,
        codeRequired: String
    ): ResultWrapper<BaseResponse> {
        return safeApiCall({
            apiService.post(headers, request.url, message)
        }, codeRequired)
    }

    suspend fun putMethod(
        headers: Map<String, String>,
        request: KeyRequest,
        message: Any? = null,
        codeRequired: String
    ): ResultWrapper<BaseResponse> {
        return safeApiCall({
            apiService.put(headers, request.url, message)
        }, codeRequired)
    }

    suspend fun deleteMethod(
        headers: Map<String, String>,
        request: KeyRequest,
        message: Any? = null,
        codeRequired: String
    ): ResultWrapper<BaseResponse> {
        return safeApiCall({
            apiService.delete(headers, request.url, message)
        }, codeRequired)
    }

    suspend fun downloadFile(
        headers: Map<String, String>,
        url: String,
        message: Any? = null,
        codeRequired: String
    ): ResultWrapper<BaseResponse> {
        return safeApiCallForLongTask({
            apiServiceFile.get(headers, url + message)
        }, codeRequired)
    }

    suspend fun uploadFile(
        headers: Map<String, String>,
        request: KeyRequest,
        file: MultipartBody.Part?,
        type: RequestBody?,
        codeRequired: String
    ): ResultWrapper<BaseResponse> {
        return safeApiCallForLongTask({
            apiServiceFile.uploadFile(
                headers = headers,
                url = request.url,
                file = file,
                type = type
            )
        }, codeRequired)
    }

    suspend fun uploadBinaryFile(
        headers: Map<String, String>,
        request: KeyRequest,
        message: RequestBody,
        codeRequired: String
    ): ResultWrapper<BaseResponse> {
        return safeApiCallForLongTask({
            apiService.uploadBinaryFile(headers, request.url, message)
        }, codeRequired)
    }

    suspend fun uploadFileLongTime(
        headers: Map<String, String>,
        request: KeyRequest,
        file: MultipartBody.Part?,
        type: RequestBody?,
        codeRequired: String
    ): ResultWrapper<BaseResponse> {
        return safeApiCallForLongTask({
            apiServiceFile.uploadFile(
                headers = headers,
                url = request.url,
                file = file,
                type = type
            )
        }, codeRequired)
    }

    suspend fun savePhotoOfParcel(
        headers: Map<String, String>,
        request: KeyRequest,
        file: MultipartBody.Part?,
        orderId: RequestBody?,
        type: RequestBody?,
        codeRequired: String
    ): ResultWrapper<BaseResponse> {
        return safeApiCallForLongTask({
            apiServiceFile.savePhotoOfParcel(
                headers = headers,
                url = request.url,
                file = file,
                type = type,
                orderId = orderId
            )
        }, codeRequired)
    }

    suspend fun uploadFileKYC(
        headers: Map<String, String>,
        request: KeyRequest,
        file: MultipartBody.Part?,
        type: RequestBody?,
        codeRequired: String
    ): ResultWrapper<BaseResponse> {
        return safeApiCallForLongTask({
            apiServiceFile.uploadFileKYC(
                headers = headers,
                url = request.url,
                file = file,
                type = type
            )
        }, codeRequired)
    }

    suspend fun uploadMultiFile(
        headers: Map<String, String>,
        request: KeyRequest,
        file: Array<MultipartBody.Part?>,
        type: RequestBody?,
        codeRequired: String
    ): ResultWrapper<BaseResponse> {
        return safeApiCallForLongTask({
            apiServiceFile.uploadMultiFile(
                headers = headers,
                url = request.url,
                file = file,
                type = type
            )
        }, codeRequired)
    }

    private suspend fun safeApiCall(
        apiCall: suspend () -> BaseResponse,
        codeRequired: String
    ): ResultWrapper<BaseResponse> =
        withTimeoutOrNull(TIME_OUT) {
            try {
                val response = apiCall.invoke()
                if (response.code == codeRequired || response.code == null) {
                    ResultWrapper.Success(response)
                } else {
                    ResultWrapper.Error(response.code, response.message, response.data())
                }
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> {
                        if (throwable.message == "NO_INTERNET") {
                            ResultWrapper.Error("NO_INTERNET", "NO_INTERNET")
                        } else {
                            ResultWrapper.Error(AppConstants.IOException, throwable.message)
                        }
                    }

                    is HttpException -> {
                        val code = throwable.code()
                        ResultWrapper.Error(code.toString(), throwable.message)
                    }

                    is TimeoutCancellationException -> {
                        ResultWrapper.Error(
                            AppConstants.TimeoutCancellationException,
                            throwable.message
                        )
                    }

                    else -> {
                        ResultWrapper.Error(AppConstants.UnknownError, throwable.message)
                    }
                }
            }
        } ?: ResultWrapper.Error(AppConstants.TimeOut, AppConstants.TimeOut)


    private suspend fun safeApiCallForLongTask(
        apiCall: suspend () -> BaseResponse,
        codeRequired: String
    ): ResultWrapper<BaseResponse> =
        withTimeoutOrNull(LONG_TIME_OUT) {
            try {
                val response = apiCall.invoke()
                if (response.code == codeRequired) {
                    ResultWrapper.Success(response)
                } else {
                    ResultWrapper.Error(response.code, response.message)
                }
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> {
                        if (throwable.message == "NO_INTERNET") {
                            ResultWrapper.Error("NO_INTERNET", "NO_INTERNET")
                        } else {
                            ResultWrapper.Error(AppConstants.IOException, throwable.message)
                        }
                    }

                    is HttpException -> {
                        val code = throwable.code()
                        ResultWrapper.Error(code.toString(), throwable.message)
                    }

                    is TimeoutCancellationException -> {
                        ResultWrapper.Error(
                            AppConstants.TimeoutCancellationException,
                            throwable.message
                        )
                    }

                    else -> {
                        ResultWrapper.Error(AppConstants.UnknownError, throwable.message)
                    }
                }
            }
        } ?: ResultWrapper.Error(AppConstants.TimeOut, AppConstants.TimeOut)
}