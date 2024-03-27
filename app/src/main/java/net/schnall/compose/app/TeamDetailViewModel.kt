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

    private val order = Order(field = OrderField.WINS, isDescending = true)

    fun loadTeams(teamId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            gameRepo.fetchGames()
                .catch { exception ->
                    _uiState.value = TeamDetailUiState.Error(message = exception.toString())
                }
                .collect { games ->
                    _uiState.value = TeamDetailUiState.Success(teams = buildTeamDetailItems(games, teamId))
                }
        }
    }

    fun updateOrder(orderField: OrderField) {
        if (order.field == orderField) {
            order.isDescending = !order.isDescending
        } else {
            order.field = orderField
        }

        _uiState.update { TeamDetailUiState.Loading(false) }
    }

    private fun reorder(teams: List<TeamDetailItem>): List<TeamDetailItem> {
            return if (order.isDescending) {
                 when (order.field) {
                    OrderField.NAME -> {
                        teams.sortedByDescending { it.teamName }
                    }
                    OrderField.WINS -> {
                        teams.sortedByDescending { it.winsAgainst }
                    }
                    OrderField.LOSSES -> {
                        teams.sortedByDescending { it.lossesAgainst }
                    }
                    OrderField.DRAWS -> {
                        teams.sortedByDescending { it.drawsAgainst }
                    }
                    OrderField.GAMES -> {
                        teams.sortedByDescending { it.totalGames }
                    }
                }
            } else {
                when (order.field) {
                    OrderField.NAME -> {
                        teams.sortedBy { it.teamName }
                    }
                    OrderField.WINS -> {
                        teams.sortedBy { it.winsAgainst }
                    }
                    OrderField.LOSSES -> {
                        teams.sortedBy { it.lossesAgainst }
                    }
                    OrderField.DRAWS -> {
                        teams.sortedBy { it.drawsAgainst }
                    }
                    OrderField.GAMES -> {
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

        return reorder(items)
    }
}

enum class OrderField {
    NAME, WINS, LOSSES, DRAWS, GAMES
}

data class Order(var field: OrderField, var isDescending: Boolean)

sealed class TeamDetailUiState {
    data class Success(val teams: List<TeamDetailItem>): TeamDetailUiState()
    data class Error(val message: String): TeamDetailUiState()
    data class Loading(val showIndicator: Boolean = false) : TeamDetailUiState()
}