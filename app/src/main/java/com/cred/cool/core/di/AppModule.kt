package com.cred.cool.core.di

import com.cred.cool.loan.data.repositoryImpl.MainRepositoryImpl
import com.cred.cool.loan.data.source.remote.ApiService
import com.cred.cool.loan.data.source.remote.network.NetworkModule
import com.cred.cool.loan.domain.repository.MainRepository
import com.cred.cool.loan.domain.usecases.FetchLoanDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService{
        return NetworkModule.createApiService()
    }

    @Provides
    @Singleton
    fun provideMainRepository(apiService: ApiService): MainRepository {
        return MainRepositoryImpl(apiService)
    }


    @Provides
    fun provideFetchLoanDataUseCase(mainRepository: MainRepository): FetchLoanDataUseCase {
        return FetchLoanDataUseCase(mainRepository)
    }

}