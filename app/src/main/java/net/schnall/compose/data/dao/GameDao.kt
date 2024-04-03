package net.schnall.compose.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import net.schnall.compose.data.Game

@Dao
interface GameDao {
    @Upsert
    suspend fun upsertAll(vararg games: Game)

    @Delete
    suspend fun delete(user: Game)

    @Query("SELECT * FROM game")
    fun getAll(): Flow<List<Game>>
}