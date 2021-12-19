package com.example.githubuserapi.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuserapi.view.detail.DetailUserViewModel
import com.example.githubuserapi.view.favorite.FavoriteViewModel

class ViewModelFactoryDB private constructor(private val mApplication: Application) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactoryDB? = null

        @JvmStatic
        fun getInstance(application: Application): ViewModelFactoryDB {
            if (INSTANCE == null) {
                synchronized(ViewModelFactoryDB::class.java) {
                    INSTANCE = ViewModelFactoryDB(application)
                }
            }
            return INSTANCE as ViewModelFactoryDB
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(DetailUserViewModel::class.java)) {
            return DetailUserViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}