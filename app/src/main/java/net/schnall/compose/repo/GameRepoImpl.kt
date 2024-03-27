package net.schnall.compose.repo

import kotlinx.coroutines.flow.flow
import net.schnall.compose.data.dao.GameDao
import net.schnall.compose.network.GameService

class GameRepoImpl(
    private val gameDao: GameDao,
    private val gameService: GameService
): GameRepo {
    override suspend fun fetchGames() = flow {
        var games = gameDao.getAll()

        if (games.isEmpty()) {
            games = gameService.games()
            gameDao.insertAll(*games.toTypedArray())
        }

        emit(games)
    }
}