package com.example.evapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class, Member::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
