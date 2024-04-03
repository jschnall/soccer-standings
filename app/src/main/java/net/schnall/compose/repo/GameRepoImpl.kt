package net.schnall.compose.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import net.schnall.compose.data.dao.GameDao
import net.schnall.compose.network.GameApi
import net.schnall.compose.repo.cache.ApiCacheHelper

class GameRepoImpl(
    private val cacheHelper: ApiCacheHelper,
    private val gameDao: GameDao,
    private val gameApi: GameApi
): GameRepo {
    override fun fetchGames(forceRefresh: Boolean) = flow {
        if (forceRefresh || cacheHelper.isExpired(CACHE_KEY)) {
            gameApi.fetchGames().flowOn(Dispatchers.IO).collect { games ->
                gameDao.deleteAll()
                gameDao.upsertAll(*games.toTypedArray())
                cacheHelper.updateFetchTime(CACHE_KEY)
                emit(games)
            }
        } else {
            gameDao.getAll().flowOn(Dispatchers.IO).collect { games ->
                emit(games)
            }
        }
    }

    companion object {
        const val CACHE_KEY: String = "GAMES_UPDATED"
    }
}