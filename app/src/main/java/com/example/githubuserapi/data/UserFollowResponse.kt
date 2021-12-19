package com.example.githubuserapi.data

import com.google.gson.annotations.SerializedName


data class UserFollow(

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("id")
    val id: Int,

    )
