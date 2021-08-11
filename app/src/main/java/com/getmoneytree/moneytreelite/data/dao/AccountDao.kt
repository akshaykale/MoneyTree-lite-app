package com.getmoneytree.moneytreelite.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.getmoneytree.moneytreelite.data.models.MAccount
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

	@Query("SELECT * FROM ${MAccount.TABLE_NAME}")
	fun getAccounts(): Flow<List<MAccount>>

	@Query("SELECT * FROM ${MAccount.TABLE_NAME} WHERE id LIKE :accountId")
	fun getAccount(accountId: Int): MAccount

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun addAccounts(accounts: List<MAccount>)

	@Query("DELETE FROM ${MAccount.TABLE_NAME}")
	fun clearAccounts()
}