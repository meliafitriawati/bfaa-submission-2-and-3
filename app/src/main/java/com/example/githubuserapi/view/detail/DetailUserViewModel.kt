package com.example.githubuserapi.view.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapi.data.UserResponse
import com.example.githubuserapi.local.Favorite
import com.example.githubuserapi.local.repository.FavoriteRepository
import com.example.githubuserapi.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : ViewModel() {
    private val mFavRepository: FavoriteRepository = FavoriteRepository(application)
    private val _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "DetailUserViewModel"
    }

    fun getUser(user: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().detailUser(user)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _user.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun insert(favorite: Favorite) {
        mFavRepository.insert(favorite)
    }

    fun delete(favorite: Favorite) {
        mFavRepository.delete(favorite)
    }

    fun getFave(id: Int) : LiveData<Boolean> {
        return mFavRepository.isRowIsExist(id)
    }
}