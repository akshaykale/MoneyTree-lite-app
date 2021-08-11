package com.getmoneytree.moneytreelite.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.getmoneytree.moneytreelite.data.dao.AccountDao
import com.getmoneytree.moneytreelite.data.dao.TransactionDao
import com.getmoneytree.moneytreelite.data.models.MAccount
import com.getmoneytree.moneytreelite.data.models.MTransaction

@Database(entities = [MAccount::class, MTransaction::class], version = 1, exportSchema = false)
internal abstract class LocalDb : RoomDatabase() {

	abstract fun getAccountDao(): AccountDao
	abstract fun getTransactionDao(): TransactionDao


	companion object {
		// Singleton prevents multiple instances of database opening at the
		// same time.
		@Volatile
		private var INSTANCE: LocalDb? = null

		fun getDatabase(context: Context): LocalDb {
			val tempInstance = INSTANCE
			if (tempInstance != null) {
				return tempInstance
			}
			synchronized(this) {
				val instance = Room.databaseBuilder(
					context,
					LocalDb::class.java,
					"local-database"
				).build()
				INSTANCE = instance
				return instance
			}
		}

		fun getDatabase(): LocalDb {
			val tempInstance = INSTANCE
			if (tempInstance != null) {
				return tempInstance
			} else throw IllegalStateException(LocalDb::class.java.simpleName + " is not initialized, call initializeInstance(..) method first.")
		}
	}
}