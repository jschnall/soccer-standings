package net.schnall.compose.repo

import net.schnall.compose.data.Game
import kotlinx.coroutines.flow.Flow

interface GameRepo {
    suspend fun fetchGames(): Flow<List<Game>>
}