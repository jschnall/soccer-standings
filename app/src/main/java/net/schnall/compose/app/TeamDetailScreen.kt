package net.schnall.compose.app

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import net.schnall.compose.R
import net.schnall.compose.data.TeamDetailItem
import net.schnall.compose.theme.ComposeStarterTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun TeamDetailScreen(
    teamId: String,
    viewModel: TeamDetailViewModel = koinViewModel(),
    onSort: (OrderField) -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.loadTeams(teamId)

    when(uiState.value) {
        is TeamDetailUiState.Success -> {
            TeamDetails(
                teams = (uiState.value as TeamDetailUiState.Success).teams,
                onSort = onSort
            )
        }

        is TeamDetailUiState.Error -> {
            with (uiState.value as TeamDetailUiState.Error) {
                Text(text = message)
            }
        }

        is TeamDetailUiState.Loading -> {
            with (uiState.value as TeamDetailUiState.Loading) {
                if (showIndicator) {
                    MyProgress()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TeamDetails(
    teams: List<TeamDetailItem>,
    onSort: (OrderField) -> Unit
) {
    LazyColumn {
        stickyHeader {
            TeamDetailHeader(onSort = onSort)
        }
        items(items = teams) { team ->
            TeamDetailListItem(
                team = team
            )
        }
    }
}

@Composable
fun TeamDetailHeader(
    onSort: (OrderField) -> Unit
) {

    TeamDetailListItem(
        name = stringResource(id = R.string.team_detail_header_name),
        winsAgainst = stringResource(id = R.string.team_detail_header_wins),
        lossesAgainst = stringResource(id = R.string.team_detail_header_losses),
        drawsAgainst = stringResource(id = R.string.team_detail_header_draws),
        totalGames = stringResource(id = R.string.team_detail_header_total_games),
        bgColor = Color.Gray,
        clickable = true,
        onClick = onSort
    )
}

@Composable
fun TeamDetailListItem(
    team: TeamDetailItem,
    modifier: Modifier = Modifier
) {
    TeamDetailListItem(
        name = team.teamName,
        winsAgainst = team.winsAgainst.toString(),
        lossesAgainst = team.lossesAgainst.toString(),
        drawsAgainst = team.drawsAgainst.toString(),
        totalGames = team.totalGames.toString()
    )
}

@Composable
fun TeamDetailListItem(
    name: String,
    winsAgainst: String,
    lossesAgainst: String,
    drawsAgainst: String,
    totalGames: String,
    modifier: Modifier = Modifier,
    bgColor: Color = Color.LightGray,
    clickable: Boolean = false,
    onClick: (OrderField) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = bgColor),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .clickable(enabled = clickable) { onClick(OrderField.NAME) }
                .padding(16.dp)
                .weight(1f),
            text = name,
            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
        )
        Text(
            modifier = Modifier
                .clickable(enabled = clickable) { onClick(OrderField.WINS) }
                .padding(16.dp)
                .defaultMinSize(minWidth = 20.dp),
            text = winsAgainst,
            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
        )
        Text(
            modifier = Modifier
                .clickable(enabled = clickable) { onClick(OrderField.LOSSES) }
                .padding(16.dp)
                .defaultMinSize(minWidth = 20.dp),
            text = lossesAgainst,
            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
        )
        Text(
            modifier = Modifier
                .clickable(enabled = clickable) { onClick(OrderField.DRAWS) }
                .padding(16.dp)
                .defaultMinSize(minWidth = 20.dp),
            text = drawsAgainst,
            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
        )
        Text(
            modifier = Modifier
                .clickable(enabled = clickable) { onClick(OrderField.GAMES) }
                .padding(16.dp)
                .defaultMinSize(minWidth = 20.dp),
            text = totalGames,
            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun TeamDetailsPreview() {
    ComposeStarterTheme {
        TeamDetails(
            listOf(
                TeamDetailItem("0", "Real Madrid", 1, 1, 0, 10),
                TeamDetailItem("1", "Arsenal", 2,0, 0, 6),
            ),
            onSort = {}
        )
    }
}