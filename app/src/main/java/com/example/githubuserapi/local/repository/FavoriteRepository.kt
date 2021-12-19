package com.example.githubuserapi.local.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubuserapi.local.Favorite
import com.example.githubuserapi.local.FavoriteDao
import com.example.githubuserapi.local.database.FavRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private var mFavDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavRoomDatabase.getDatabase(application)
        mFavDao = db.favDao()
    }

    fun getAllFaves(): LiveData<List<Favorite>> = mFavDao.getAllFavorites()

    fun isRowIsExist(id: Int): LiveData<Boolean> = mFavDao.isRowIsExist(id)

    fun insert(fave: Favorite) {
        executorService.execute { mFavDao.insert(fave) }
    }

    fun delete(fave: Favorite) {
        executorService.execute { mFavDao.delete(fave) }
    }

}