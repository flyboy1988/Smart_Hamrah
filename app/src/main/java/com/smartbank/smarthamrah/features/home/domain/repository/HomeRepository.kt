package com.smartbank.smarthamrah.features.home.domain.repository

import com.smartbank.smarthamrah.features.home.data.model.HomeRecentRequest
import com.smartbank.smarthamrah.features.home.data.model.HomeStats
import kotlinx.coroutines.flow.Flow

// features/home/domain/repository/HomeRepository.kt
interface HomeRepository {
    fun getStats(): Flow<HomeStats>
    fun getRecentRequests(): Flow<List<HomeRecentRequest>>
}