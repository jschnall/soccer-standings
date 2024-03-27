package net.schnall.compose.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import net.schnall.compose.repo.GameRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import net.schnall.compose.data.Game
import net.schnall.compose.data.TeamItem

class TeamListViewModel(private val gameRepo: GameRepo) : ViewModel() {
    private val _uiState = MutableStateFlow<TeamListUiState>(TeamListUiState.Loading(true))
    val uiState: StateFlow<TeamListUiState>
        get() = _uiState

    init {
        loadTeams()
    }

    fun loadTeams() {
        _uiState.value = TeamListUiState.Loading()

        viewModelScope.launch {
            gameRepo.fetchGames()
                .catch { exception ->
                    _uiState.value = TeamListUiState.Error(message = exception.toString())
                }
                .collect { games ->
                    _uiState.value = TeamListUiState.Success(teams = buildTeamItems(games))
                }
        }
    }

    private fun buildTeamItems(games: List<Game>): List<TeamItem> {
        val nameMap = mutableMapOf<String, String>()
        val winMap = mutableMapOf<String, Int>()
        val lossMap = mutableMapOf<String, Int>()
        val drawMap = mutableMapOf<String, Int>()

        games.forEach { game ->
            // Assume data is well formatted, and there aren't multiple names to an id,
            // otherwise only the last name found will be used
            nameMap[game.awayTeamId] = game.awayTeamName
            nameMap[game.homeTeamId] = game.homeTeamName

            if (game.awayTeamScore == game.homeTeamScore) {
                drawMap[game.awayTeamId] = drawMap.getOrDefault(game.awayTeamId, 0) + 1
                drawMap[game.homeTeamId] = drawMap.getOrDefault(game.homeTeamId, 0) + 1
            } else if (game.awayTeamScore > game.homeTeamScore) {
                winMap[game.awayTeamId] = winMap.getOrDefault(game.awayTeamId, 0) + 1
                lossMap[game.homeTeamId] = lossMap.getOrDefault(game.homeTeamId, 0) + 1
            } else {
                lossMap[game.awayTeamId] = lossMap.getOrDefault(game.awayTeamId, 0) + 1
                winMap[game.homeTeamId] = winMap.getOrDefault(game.homeTeamId, 0) + 1
            }
        }

        return nameMap.map { (key, value) ->
            val wins = winMap[key] ?: 0
            val losses = lossMap[key] ?: 0
            val draws = drawMap[key] ?: 0
            TeamItem(
                teamId = key,
                teamName = value,
                wins = wins,
                losses = losses,
                draws = draws,
                winPercentage = 100 * wins / (wins + losses + draws).toDouble()
            )
        }.sortedByDescending { it.winPercentage }
    }
}

sealed class TeamListUiState {
    data class Success(val teams: List<TeamItem>): TeamListUiState()
    data class Error(val message: String): TeamListUiState()
    data class Loading(val showIndicator: Boolean = false) : TeamListUiState()
}