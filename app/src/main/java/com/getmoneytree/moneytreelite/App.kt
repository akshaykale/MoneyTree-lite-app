package com.getmoneytree.moneytreelite

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.getmoneytree.moneytreelite.data.LocalDb
import com.getmoneytree.moneytreelite.utils.GetAssets

class App : Application() {

	override fun onCreate() {
		super.onCreate()

		AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

		GetAssets.getAssetManager(applicationContext)
		LocalDb.getDatabase(applicationContext)
	}
}