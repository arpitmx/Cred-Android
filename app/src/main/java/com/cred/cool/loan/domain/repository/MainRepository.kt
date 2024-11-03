package com.cred.cool.loan.domain.repository

import UiResponse
import com.cred.cool.core.domain.ServerResponse

interface MainRepository {
    suspend fun getUIData(): ServerResponse<UiResponse>
}