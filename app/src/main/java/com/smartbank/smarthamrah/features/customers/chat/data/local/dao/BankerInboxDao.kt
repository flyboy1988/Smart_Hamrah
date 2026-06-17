package com.smartbank.smarthamrah.features.customers.chat.data.local.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smartbank.smarthamrah.features.customers.chat.data.local.entity.BankerInboxEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BankerInboxDao {

    @Query("""
        SELECT * FROM banker_inbox
        ORDER BY 
            CASE WHEN lastMessageAtUtc IS NULL THEN 1 ELSE 0 END,
            lastMessageAtUtc DESC
    """)
    fun observeInbox(): Flow<List<BankerInboxEntity>>

    @Query("SELECT * FROM banker_inbox WHERE customerId = :customerId LIMIT 1")
    suspend fun getByCustomerId(customerId: Long): BankerInboxEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<BankerInboxEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: BankerInboxEntity)

    @Query("UPDATE banker_inbox SET unreadCount = 0 WHERE customerId = :customerId")
    suspend fun clearUnread(customerId: Long)

    @Query("DELETE FROM banker_inbox")
    suspend fun clearAll()
}