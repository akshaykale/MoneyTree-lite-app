package com.getmoneytree.moneytreelite.ui.accounts

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.getmoneytree.moneytreelite.data.LocalDb
import com.getmoneytree.moneytreelite.data.dao.AccountDao
import com.getmoneytree.moneytreelite.data.models.Currency
import com.getmoneytree.moneytreelite.data.models.MAccount
import com.getmoneytree.moneytreelite.ui.BaseViewModel
import com.getmoneytree.moneytreelite.utils.SingleLiveEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class AccountsViewModel(
    private val accountsDao: AccountDao = LocalDb.getDatabase().getAccountDao()
) : BaseViewModel() {

    sealed class AccountsEvents {
        object OnAddAccountClicked : AccountsEvents()
        data class OnAccountClicked(val account: MAccount) : AccountsEvents()
        data class AccountsLoaded(
            val data: List<InstitutionAccounts>,
            val totalAmount: Double,
            val currency: String
        ) : AccountsEvents()
    }

    private val _events = SingleLiveEvent<AccountsEvents>()
    val events: LiveData<AccountsEvents>
        get() = _events

    val totalBalanceText = ObservableField("")

    @ExperimentalCoroutinesApi
    fun loadData() = viewModelScope.launch {
        accountsDao.getAccounts()
            .onStart {
                startProcessing(text = "Loading accounts...")
            }
            .catch {
                stopProcessing()
            }
            .collect { accounts ->
                _events.value = processAccounts(accounts)
                stopProcessing()
            }
    }

    private fun processAccounts(accounts: List<MAccount>): AccountsEvents.AccountsLoaded {
        var totalBal = 0.0
        accounts.groupBy { it.institution }.toSortedMap().let { map ->
            val data = mutableListOf<InstitutionAccounts>()
            map.entries.map { entry ->
                var amount = 0.0
                var currency: String = Currency.JPY.value
                entry.value.forEach {
                    amount += it.current_balance_in_base
                }
                totalBal += amount
                data.add(
                    InstitutionAccounts(
                        institutionName = entry.key,
                        accounts = entry.value.sortedBy { it.name },
                        amount = amount,
                        currency = currency
                    )
                )
            }
            totalBalanceText.set("${Currency.JPY.value} $totalBal")
            return AccountsEvents.AccountsLoaded(
                data,
                totalAmount = totalBal,
                currency = Currency.JPY.value
            )
        }
    }

    fun onAddAccountClick() {
        _events.value = AccountsEvents.OnAddAccountClicked
    }

    fun onAccountClick(acc: MAccount) {
        _events.value = AccountsEvents.OnAccountClicked(acc)
    }
}
