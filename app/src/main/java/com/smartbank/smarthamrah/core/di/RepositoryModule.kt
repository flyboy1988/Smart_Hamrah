package com.smartbank.smarthamrah.core.di



import com.smartbank.smarthamrah.features.home.data.repositoryImpl.HomeRepositoryImpl
import com.smartbank.smarthamrah.features.home.domain.repository.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        impl: HomeRepositoryImpl
    ): HomeRepository
}