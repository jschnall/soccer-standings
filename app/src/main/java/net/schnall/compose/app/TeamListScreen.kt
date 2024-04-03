package net.schnall.compose.app

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.schnall.compose.R
import net.schnall.compose.data.TeamItem
import net.schnall.compose.app.theme.ComposeStarterTheme
import java.text.DecimalFormat

@Composable
fun TeamListScreen(
    uiState: TeamListUiState,
    onTeamClick: (String, String) -> Unit,
    showSnackbar: (String, SnackbarDuration) -> Unit,
    onRefresh: () -> Unit
) {

    when(uiState) {
        is TeamListUiState.Success -> {
            TeamList(
                teams = uiState.teams,
                onTeamClick = onTeamClick,
                refreshing = uiState.refreshing,
                onRefresh = onRefresh
            )
        }

        is TeamListUiState.Error -> {
            showSnackbar(uiState.message, SnackbarDuration.Short)
        }

        is TeamListUiState.Loading -> {
            MyProgress()
        }
    }
}

// TODO extract to commonUi
@Composable
fun MyProgress() {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(64.dp)
                .height(64.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(R.string.please_wait)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun TeamList(
    teams: List<TeamItem>,
    onTeamClick: (String, String) -> Unit,
    refreshing: Boolean,
    onRefresh: () -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = onRefresh)

    Box(
        Modifier
            .pullRefresh(pullRefreshState)
    ) {
        LazyColumn {
            stickyHeader {
                TeamListHeader()
            }
            items(items = teams) { team ->
                TeamListItem(
                    team = team,
                    onClick = { onTeamClick(team.teamId, team.teamName) }
                )
            }
        }

        PullRefreshIndicator(refreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
fun TeamListHeader() {
    TeamListItem(
        name = stringResource(id = R.string.team_list_header_name),
        wins = stringResource(id = R.string.team_list_header_wins),
        losses = stringResource(id = R.string.team_list_header_losses),
        draws = stringResource(id = R.string.team_list_header_draws),
        winPercentage = stringResource(id = R.string.team_list_header_win_percent),
        bgColor = Color.Gray,
        clickable = false
    )
}

@Composable
fun TeamListItem(
    team: TeamItem,
    decimalFormat: DecimalFormat = DecimalFormat("###.##"),
    onClick: () -> Unit
) {
    TeamListItem(
        name = team.teamName,
        wins = team.wins.toString(),
        losses = team.losses.toString(),
        draws = team.draws.toString(),
        winPercentage = decimalFormat.format(team.winPercentage),
        onClick = onClick
    )
}

@Composable
fun TeamListItem(
    name: String,
    wins: String,
    losses: String,
    draws: String,
    winPercentage: String,
    onClick: () -> Unit = {},
    clickable: Boolean = true,
    bgColor: Color = Color.Transparent
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = bgColor)
            .clickable(enabled = clickable) { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f),
            text = name,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            modifier = Modifier
                .padding(16.dp)
                .defaultMinSize(minWidth = 20.dp),
            text = wins,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            modifier = Modifier
                .padding(16.dp)
                .defaultMinSize(minWidth = 20.dp),
            text = losses,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            modifier = Modifier
                .padding(16.dp)
                .defaultMinSize(minWidth = 20.dp),
            text = draws,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            modifier = Modifier
                .padding(16.dp)
                .defaultMinSize(minWidth = 40.dp),
            text = winPercentage,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun TeamListPreview() {
    ComposeStarterTheme {
        TeamList(
            teams = listOf(
                TeamItem("0", "Real Madrid", 1, 1, 0, 50.515),
                TeamItem("1", "Arsenal", 2,0, 0, 100.0),
            ),
            onTeamClick = { _, _ -> },
            refreshing = false,
            onRefresh = {}
        )
    }
}