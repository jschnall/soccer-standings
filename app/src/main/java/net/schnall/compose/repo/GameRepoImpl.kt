package net.schnall.compose.repo

import kotlinx.coroutines.flow.flow
import net.schnall.compose.data.Game
import net.schnall.compose.network.GameService

class GameRepoImpl(private val gameService: GameService): GameRepo {
    private var games: List<Game> = emptyList()

    override fun fetchGames() = flow {
        if (games.isEmpty()) {
            games = gameService.games()
        }
        emit(games)
    }
}