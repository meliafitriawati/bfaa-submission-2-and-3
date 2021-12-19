package com.example.githubuserapi.network

import com.example.githubuserapi.data.UserFollow
import com.example.githubuserapi.data.UserResponse
import com.example.githubuserapi.data.UsersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun findUsers(
        @Query("q") username: String
    ): Call<UsersResponse>

    @GET("users/{username}")
    fun detailUser(
        @Path("username") username: String
    ): Call<UserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<UserFollow>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<UserFollow>>
}