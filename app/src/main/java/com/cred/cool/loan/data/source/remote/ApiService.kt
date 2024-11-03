package com.cred.cool.loan.data.source.remote

import UiResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("/p6764/test_mint")
    suspend fun getLoanData(): Response<UiResponse>
}