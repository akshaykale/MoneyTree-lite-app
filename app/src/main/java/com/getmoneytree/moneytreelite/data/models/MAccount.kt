package com.getmoneytree.moneytreelite.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * {
"id": 1,
"name": "外貨普通(USD)",
"institution": "Test Bank",
"currency": "USD",
"current_balance": 22.5,
"current_balance_in_base": 2306.0
}
 */
@Entity(tableName = "account_table")
data class MAccount(
	@PrimaryKey var id: Int = 0,
	var name: String = "",
	var institution: String = "",
	var currency: String = "",
	var current_balance: Double = 0.0,
	var current_balance_in_base: Double = 0.0
): Serializable {
	companion object{
		const val TABLE_NAME: String = "account_table"
	}

	fun balance(): String = "$currency $current_balance"
}

data class MAccountList(
	val accounts: List<MAccount>
)