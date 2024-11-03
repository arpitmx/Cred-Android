package com.cred.cool.loan.domain.usecases

import UiResponse
import com.cred.cool.core.domain.ServerResponse
import com.cred.cool.loan.domain.repository.MainRepository

class FetchLoanDataUseCase (private val repository: MainRepository) {
    suspend fun execute(): ServerResponse<UiResponse>  {
        return repository.getUIData()
    }
}