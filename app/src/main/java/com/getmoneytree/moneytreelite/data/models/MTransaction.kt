package com.getmoneytree.moneytreelite.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

/**
 *
{
"account_id": 1,
"amount": -6850.0,
"category_id": 192,
"date": "2017-08-22T00:00:00+09:00",
"description": "\u53d6\u5f15\u660e\u7d30\u540d 2017-08-22T17:19:31+09:00",
"id": 11
}
 */

@Entity(tableName = "transaction_table")
//@TypeConverters(Converters::class)
data class MTransaction(
    @PrimaryKey var id: Int = 0,
    var account_id: Int = 0,
    var amount: Double = 0.0,
    var category_id: Int = 0,
    var date: String = "",
    var description: String = "",
) {
    companion object {
        const val TABLE_NAME = "transaction_table"
    }

    fun getCalendar(): Calendar {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-mm-dd")
        cal.time = sdf.parse(date.split("T")[0])
        return cal
    }

    fun getMonthYear(): String {
        return getCalendar().let {
            val month = SimpleDateFormat("MMM").format(it.time)
            val year = it.get(Calendar.YEAR)
            "$month $year"
        }
    }

    fun getSingleDate(): String {
        return getCalendar().get(Calendar.DAY_OF_MONTH).let {
            when(it) {
                1 or 21 or 31 -> {
                    "${it}st"
                }
                2 or 22 -> {
                    "${it}nd"
                }
                else -> "${it}th"
            }
        }
    }
}

data class MTransactionList(
    val transactions: List<MTransaction>
)

/*object Converters {
    @TypeConverter
    fun fromDate(value: String?): Calendar? {
        return value?.let {
            val cal = Calendar.getInstance()
            val sdf = SimpleDateFormat()
            cal.time = sdf.parse(value)
            cal
        }
    }

    @TypeConverter
    fun calendarToString(date: Calendar?): String? {
        return date?.toString()
    }
}*/
