package com.smartbank.smarthamrah.core.database


import android.content.Context
import androidx.room.Room
import com.smartbank.smarthamrah.features.customers.chat.data.local.dao.BankerInboxDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideSmartBankDatabase(
        @ApplicationContext context: Context
    ): SmartBankDatabase {
        return Room.databaseBuilder(
            context,
            SmartBankDatabase::class.java,
            "smartbank.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideBankerInboxDao(
        database: SmartBankDatabase
    ): BankerInboxDao {
        return database.bankerInboxDao()
    }
}