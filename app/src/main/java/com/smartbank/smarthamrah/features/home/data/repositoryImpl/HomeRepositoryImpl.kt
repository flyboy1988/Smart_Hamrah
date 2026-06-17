package com.smartbank.smarthamrah.features.home.data.repositoryImpl

import com.smartbank.smarthamrah.features.home.domain.repository.HomeRepository
import com.smartbank.smarthamrah.features.home.data.model.HomeStats
import com.smartbank.smarthamrah.features.home.data.model.HomeRecentRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.random.Random

class HomeRepositoryImpl @Inject constructor() : HomeRepository {

    override fun getStats(): Flow<HomeStats> = flow {
        // Simulate network delay (300-800ms)
        delay(Random.nextLong(300, 800))

        emit(
            HomeStats(
                newTickets = Random.nextInt(1, 8),      // Random between 1-7 new tickets
                monthlyContacts = Random.nextInt(100, 250), // Random between 100-249
                monthlyReports = 50    // Random between 80-199
            )
        )
    }

    override fun getRecentRequests(): Flow<List<HomeRecentRequest>> = flow {
        // Simulate network delay (500-1000ms)
        delay(Random.nextLong(1000, 2500))

//        emit(
//            listOf(
//                HomeRecentRequest("1", "پرهام رجب علی", badgeNumber = Random.nextInt(1, 6),),
//                HomeRecentRequest("2", "سارا محمدی", badgeNumber = Random.nextInt(1, 6),),
//                HomeRecentRequest("3", "رضا کریمی", badgeNumber = Random.nextInt(1, 6),),
//                HomeRecentRequest("4", "نرگس احمدی", badgeNumber = Random.nextInt(1, 6),),
//                HomeRecentRequest("5", "علی رضایی", badgeNumber = Random.nextInt(1, 6),),
//                HomeRecentRequest("6", "زهرا حسینی", badgeNumber = Random.nextInt(1, 6),),
//                HomeRecentRequest("7", "محمد نوری", badgeNumber = Random.nextInt(1, 6),),
//                HomeRecentRequest("8", "فاطمه قاسمی", badgeNumber = Random.nextInt(1, 6),)
//            )
//        )
    }
}