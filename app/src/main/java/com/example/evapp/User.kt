package com.example.evapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val username: String,
    val password: String,
    val name: String,
    val email: String
)

@Entity(tableName = "members")
data class Member(
    @PrimaryKey val username: String,
    val password: String,
    val name: String,
    val email: String
)
