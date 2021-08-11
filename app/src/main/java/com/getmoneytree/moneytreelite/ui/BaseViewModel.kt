package com.getmoneytree.moneytreelite.ui

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.getmoneytree.moneytreelite.utils.SingleLiveEvent

open class BaseViewModel: ViewModel() {

    //For progressBar
    val progressBarActive = ObservableBoolean(false)
    val progressBarText = ObservableField<String>("Loading...")

    open fun stopProcessing() = progressBarActive.set(false)
    open fun startProcessing(isProcessing: Boolean = true, text: String = "Loading...") {
        progressBarText.set(text)
        progressBarActive.set(isProcessing)
    }

    private val _onBackPress = SingleLiveEvent<Unit>()
    val backPress: LiveData<Unit>
        get() = _onBackPress

    fun onBackPress() {
        _onBackPress.value = Unit
    }
}