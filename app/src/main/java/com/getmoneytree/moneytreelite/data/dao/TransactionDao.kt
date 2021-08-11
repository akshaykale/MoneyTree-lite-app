package com.getmoneytree.moneytreelite.data.dao

import androidx.room.*
import com.getmoneytree.moneytreelite.data.models.MTransaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

	@Query("SELECT * FROM ${MTransaction.TABLE_NAME} WHERE account_id like :accountId" )
	fun getTransactions(accountId: Int): Flow<List<MTransaction>>

	@Query("SELECT * FROM ${MTransaction.TABLE_NAME} WHERE id like :tid" )
	suspend fun getTransaction(tid: Int): MTransaction

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun addTransactions(transactions: List<MTransaction>)

	@Delete
	suspend fun deleteTransaction(transaction: MTransaction)
}