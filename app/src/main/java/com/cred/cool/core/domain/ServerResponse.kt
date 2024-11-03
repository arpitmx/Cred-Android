package com.cred.cool.core.domain

sealed class ServerResponse<out T>{
    data class Success<out T>(val data : T): ServerResponse<T>()
    data class Failure(val reason : String) : ServerResponse<Nothing>()
    data object Loading : ServerResponse<Nothing>()
}