package net.schnall.compose.repo

import net.schnall.compose.data.Game
import kotlinx.coroutines.flow.Flow

interface GameRepo {
    fun fetchGames(forceRefresh: Boolean = false): Flow<List<Game>>
}