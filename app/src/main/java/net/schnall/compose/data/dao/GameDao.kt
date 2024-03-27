package net.schnall.compose.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import net.schnall.compose.data.Game

@Dao
interface GameDao {
    @Insert
    suspend fun insertAll(vararg games: Game)

    @Delete
    suspend fun delete(user: Game)

    @Query("SELECT * FROM game")
    suspend fun getAll(): List<Game>
}