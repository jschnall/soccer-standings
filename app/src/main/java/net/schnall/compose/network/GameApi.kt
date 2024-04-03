package net.schnall.compose.network

import net.schnall.compose.data.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface GameApi {
    fun fetchGames(): Flow<List<Game>>
}

class GameApiImpl(private val gameService: GameService): GameApi {
    override fun fetchGames() = flow {
        emit(gameService.games())
    }
}