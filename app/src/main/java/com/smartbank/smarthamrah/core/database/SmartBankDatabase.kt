package com.smartbank.smarthamrah.core.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.smartbank.smarthamrah.features.customers.chat.data.local.dao.BankerInboxDao
import com.smartbank.smarthamrah.features.customers.chat.data.local.entity.BankerInboxEntity

@Database(
    entities = [
        BankerInboxEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SmartBankDatabase : RoomDatabase() {

    abstract fun bankerInboxDao(): BankerInboxDao
}