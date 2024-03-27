package net.schnall.compose.data

import androidx.room.Database
import androidx.room.RoomDatabase
import net.schnall.compose.data.dao.GameDao

@Database(entities = [Game::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun gameDao(): GameDao
}