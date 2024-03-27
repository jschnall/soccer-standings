package net.schnall.compose.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import net.schnall.compose.repo.GameRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.schnall.compose.data.Game
import net.schnall.compose.data.TeamDetailItem

class TeamDetailViewModel(private val gameRepo: GameRepo) : ViewModel() {
    private val _uiState = MutableStateFlow<TeamDetailUiState>(TeamDetailUiState.Loading(true))
    val uiState = _uiState.asStateFlow()

    private val sort = Sort(field = SortField.WINS, isDescending = true)

    fun loadTeams(teamId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            gameRepo.fetchGames()
                .catch { exception ->
                    _uiState.value = TeamDetailUiState.Error(message = exception.toString())
                }
                .collect { games ->
                    _uiState.value = TeamDetailUiState.Success(
                        teams = buildTeamDetailItems(games, teamId),
                        sort.field,
                        sort.isDescending
                    )
                }
        }
    }

    fun updateSort(sortField: SortField) {
        if (sort.field == sortField) {
            sort.isDescending = !sort.isDescending
        } else {
            sort.field = sortField
        }

        _uiState.update { TeamDetailUiState.Loading(false) }
    }

    private fun sort(teams: List<TeamDetailItem>): List<TeamDetailItem> {
            return if (sort.isDescending) {
                 when (sort.field) {
                    SortField.NAME -> {
                        teams.sortedByDescending { it.teamName }
                    }
                    SortField.WINS -> {
                        teams.sortedByDescending { it.winsAgainst }
                    }
                    SortField.LOSSES -> {
                        teams.sortedByDescending { it.lossesAgainst }
                    }
                    SortField.DRAWS -> {
                        teams.sortedByDescending { it.drawsAgainst }
                    }
                    SortField.GAMES -> {
                        teams.sortedByDescending { it.totalGames }
                    }
                }
            } else {
                when (sort.field) {
                    SortField.NAME -> {
                        teams.sortedBy { it.teamName }
                    }
                    SortField.WINS -> {
                        teams.sortedBy { it.winsAgainst }
                    }
                    SortField.LOSSES -> {
                        teams.sortedBy { it.lossesAgainst }
                    }
                    SortField.DRAWS -> {
                        teams.sortedBy { it.drawsAgainst }
                    }
                    SortField.GAMES -> {
                        teams.sortedBy { it.totalGames }
                    }
                }
            }
    }

    private fun buildTeamDetailItems(games: List<Game>, teamId: String): List<TeamDetailItem> {
        val items = games.filter { it.awayTeamId == teamId || it.homeTeamId == teamId }
            .groupBy { if (it.awayTeamId == teamId) it.homeTeamId else it.awayTeamId }
            .map { (key, value) ->
                val (home, away) = value.partition { it.awayTeamId == teamId }

                TeamDetailItem(
                    teamId = key,
                    teamName = if (value[0].awayTeamId == teamId) value[0].homeTeamName else value[0].awayTeamName,
                    winsAgainst = home.count { it.homeTeamScore > it.awayTeamScore } + away.count { it.awayTeamScore > it.homeTeamScore },
                    lossesAgainst = home.count { it.homeTeamScore < it.awayTeamScore } + away.count { it.awayTeamScore < it.homeTeamScore },
                    drawsAgainst = value.count { it.homeTeamScore == it.awayTeamScore },
                    totalGames = value.size,
                )
            }

        return sort(items)
    }
}

enum class SortField {
    NAME, WINS, LOSSES, DRAWS, GAMES
}

data class Sort(var field: SortField, var isDescending: Boolean)

sealed class TeamDetailUiState {
    data class Success(val teams: List<TeamDetailItem>, val sortField: SortField, val isDescending: Boolean): TeamDetailUiState()
    data class Error(val message: String): TeamDetailUiState()
    data class Loading(val showIndicator: Boolean = false) : TeamDetailUiState()
}