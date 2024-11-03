package com.cred.cool.loan.data.repositoryImpl

import UiResponse
import com.cred.cool.core.domain.ServerResponse
import com.cred.cool.loan.data.source.remote.ApiService
import com.cred.cool.loan.domain.repository.MainRepository
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class MainRepositoryImpl (val apiService: ApiService) : MainRepository {

    override suspend fun getUIData(): ServerResponse<UiResponse> {
        return try {
            val response = apiService.getLoanData()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    ServerResponse.Success(body)
                } else {
                    ServerResponse.Failure("Empty body")
                }
            } else {
                ServerResponse.Failure("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: IOException) {
            Timber.tag("getUIData").e(e, "Network error")
            ServerResponse.Failure("Network error: ${e.message}")
        } catch (e: HttpException) {
            Timber.tag("getUIData").e(e, "HTTP error")
            ServerResponse.Failure("HTTP error: ${e.message()}")
        } catch (e: Exception) {
            Timber.tag("getUIData").e(e, "Unexpected error")
            ServerResponse.Failure("Unexpected error: ${e.message}")
        }
    }
}