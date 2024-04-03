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

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): Flow<List<Game>>

    companion object {
        const val TABLE_NAME = "game"
    }
}