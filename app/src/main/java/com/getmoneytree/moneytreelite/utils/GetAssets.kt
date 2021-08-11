package com.getmoneytree.moneytreelite.utils

import android.content.Context
import android.content.res.AssetManager
import androidx.room.Room
import com.getmoneytree.moneytreelite.data.LocalDb

class GetAssets {

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AssetManager? = null

        fun getAssetManager(context: Context): AssetManager {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = context.assets
                INSTANCE = instance
                return instance
            }
        }

        fun getAssetManager(): AssetManager {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            } else throw IllegalStateException(LocalDb::class.java.simpleName + " is not initialized, call initializeInstance(..) method first.")
        }
    }

}