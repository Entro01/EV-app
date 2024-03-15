package com.example.evapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val username: String, val password: String, val name: String, val email: String
)

@Entity(tableName = "members")
data class Member(
    @PrimaryKey val username: String, val password: String, val name: String, val email: String
)

@Entity(tableName = "stations")
data class Station(
    @PrimaryKey val stationName: String, val price: String, val latitude: String, val longitude: String, val slot: String
)

@Entity(tableName = "bookings")
data class Bookings(
    @PrimaryKey val userName: String, val carModel: String, val vehicleNumber: String, val phoneNumber: String, val time: String, val date: String, val slot: String
)