package com.example.githubuserapi.view.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapi.local.Favorite
import com.example.githubuserapi.local.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavRepository: FavoriteRepository = FavoriteRepository(application)

    fun getAllFavorite(): LiveData<List<Favorite>> = mFavRepository.getAllFaves()

    fun delete(favorite: Favorite) {
        mFavRepository.delete(favorite)
    }
}