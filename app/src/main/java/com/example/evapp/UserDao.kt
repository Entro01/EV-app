package com.example.evapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    // Check if a user exists by username
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE username = :username and password = :password)")
    fun userExists(username: String, password: String): Boolean

    // Insert a new user
    @Query("INSERT INTO users (username, password, name, email) VALUES (:username, :password, :name, :email)")
    fun insertUser(username: String, password: String, name: String, email: String)

    // Check if a user exists by username
    @Query("SELECT EXISTS(SELECT 1 FROM members WHERE username = :username and password = :password)")
    fun memberExists(username: String, password: String): Boolean

    // Insert a new user
    @Query("INSERT INTO members (username, password, name, email) VALUES (:username, :password, :name, :email)")
    fun insertMember(username: String, password: String, name: String, email: String)

    @Query("SELECT * FROM stations")
    fun getStations(): List<Station>

    @Insert(Station::class)
    fun insertStation(station: Station);

    @Delete(Station::class)
    fun deleteStation(station: Station)

    @Insert(Booking::class)
    fun insertBooking(booking: Booking)

    @Query("SELECT * FROM bookings")
    fun getBookings(): List<Booking>
}
