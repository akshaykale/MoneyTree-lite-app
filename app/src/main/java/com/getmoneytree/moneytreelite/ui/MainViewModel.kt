package com.getmoneytree.moneytreelite.ui

import android.content.res.AssetManager
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.viewModelScope
import com.getmoneytree.moneytreelite.data.LocalDb
import com.getmoneytree.moneytreelite.data.models.MAccountList
import com.getmoneytree.moneytreelite.data.models.MTransactionList
import com.getmoneytree.moneytreelite.utils.GetAssets
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException


class MainViewModel(
    private val assetManager: AssetManager = GetAssets.getAssetManager()
) : BaseViewModel() {

    val isDataProcessing = ObservableBoolean(false)

    fun loadDataInToDb() = viewModelScope.launch {
        isDataProcessing.set(true)

        val localDb = LocalDb.getDatabase()
        val gson = Gson()

        withContext(Dispatchers.IO) {

            try {
                val data: MAccountList =
                    gson.fromJson(getJsonDataFromAsset("accounts.json"), MAccountList::class.java)
                localDb.getAccountDao().addAccounts(data.accounts)

                data.accounts.map {
                    val transactionData: MTransactionList = gson.fromJson(
                        getJsonDataFromAsset("transactions_${it.id}.json"),
                        MTransactionList::class.java
                    )
                    localDb.getTransactionDao().addTransactions(transactionData.transactions)
                }
                isDataProcessing.set(false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getJsonDataFromAsset(fileName: String): String? {
        val jsonString: String
        try {
            jsonString = assetManager.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}
