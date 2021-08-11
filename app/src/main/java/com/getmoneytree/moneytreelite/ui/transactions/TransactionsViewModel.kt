package com.getmoneytree.moneytreelite.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.getmoneytree.moneytreelite.data.LocalDb
import com.getmoneytree.moneytreelite.data.dao.TransactionDao
import com.getmoneytree.moneytreelite.data.models.MAccount
import com.getmoneytree.moneytreelite.data.models.MTransaction
import com.getmoneytree.moneytreelite.ui.BaseViewModel
import com.getmoneytree.moneytreelite.utils.SingleLiveEvent
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.*

class TransactionsViewModel(
    private val transactionDao: TransactionDao = LocalDb.getDatabase().getTransactionDao()
) : BaseViewModel() {

    sealed class TransactionsEvents {
        data class TransactionsLoaded(
            val data: List<TransactionItem>
        ) : TransactionsEvents()

        data class DeleteTransaction(val transaction: MTransaction) : TransactionsEvents()
        object DeleteTransactionSuccess : TransactionsEvents()
    }

    private val _events = SingleLiveEvent<TransactionsEvents>()
    val events: LiveData<TransactionsEvents>
        get() = _events

    private var _account: MAccount? = null
    fun loadData(account: MAccount) = viewModelScope.launch {
        _account = account
        transactionDao.getTransactions(account.id)
            .onStart {
                startProcessing(text = "Loading transactions...")
            }
            .catch {
                stopProcessing()
            }
            .collect { transactions ->

                val data = mutableListOf<TransactionItem>()
                var count = 0
                transactions.groupBy { t ->
                    t.getMonthYear()
                }.map { groupedTransactions ->
                    count++
                    var amountIn = 0.0
                    var amountOut = 0.0
                    val sortedTransactions =
                        groupedTransactions.value.sortedBy { "${it.date?.get(Calendar.DAY_OF_MONTH)}" }
                    sortedTransactions.forEach {
                        if (it.amount >= 0) amountIn += it.amount
                        else amountOut += it.amount
                    }
                    data.add(
                        TransactionItem(
                            diff = count,
                            isHeader = true,
                            accountId = account.id,
                            titleText = groupedTransactions.key,
                            amountIn = amountIn,
                            amountOut = amountOut,
                            currency = account.currency
                        )
                    )
                    sortedTransactions.forEach { transaction ->
                        count++
                        data.add(
                            TransactionItem(
                                diff = count,
                                isHeader = false,
                                accountId = account.id,
                                transaction = transaction
                            )
                        )
                    }

                }

                _events.value = TransactionsEvents.TransactionsLoaded(data)
            }
    }

    fun onDeleteTransaction(tran: MTransaction) {
        _events.value = TransactionsEvents.DeleteTransaction(tran)
    }

    fun deleteTransaction(tran: MTransaction) = viewModelScope.launch {
        transactionDao.deleteTransaction(tran)
        _account?.let { loadData(it) } //reload this this account transactions
        _events.value = TransactionsEvents.DeleteTransactionSuccess
    }

}