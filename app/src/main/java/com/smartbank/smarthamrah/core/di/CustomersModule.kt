package com.smartbank.smarthamrah.core.di

import com.smartbank.smarthamrah.features.customers.data.remote.CustomersApi
import com.smartbank.smarthamrah.features.customers.data.repository.CustomersRepositoryImpl
import com.smartbank.smarthamrah.features.customers.domain.repository.CustomersRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CustomersApiModule {

    @Provides
    @Singleton
    fun provideCustomersApi(
        retrofit: Retrofit
    ): CustomersApi {
        return retrofit.create(CustomersApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class CustomersRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCustomersRepository(
        impl: CustomersRepositoryImpl
    ): CustomersRepository
}
