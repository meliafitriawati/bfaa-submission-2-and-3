package com.example.githubuserapi.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubuserapi.local.Favorite
import com.example.githubuserapi.local.FavoriteDao

@Database(entities = [Favorite::class], version = 1)
abstract class FavRoomDatabase : RoomDatabase() {
    abstract fun favDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: FavRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavRoomDatabase {
            if (INSTANCE == null) {
                synchronized(FavRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavRoomDatabase::class.java, "fav_database"
                    )
                        .build()
                }
            }
            return INSTANCE as FavRoomDatabase
        }
    }
}