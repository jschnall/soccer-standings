package net.schnall.compose.app

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.schnall.compose.R
import net.schnall.compose.data.TeamDetailItem
import net.schnall.compose.theme.ComposeStarterTheme

@Composable
fun TeamDetailScreen(
    teamId: String,
    uiState: TeamDetailUiState,
    onSort: (SortField) -> Unit,
) {
    when(uiState) {
        is TeamDetailUiState.Success -> {
            val state = (uiState as TeamDetailUiState.Success)

            TeamDetails(
                teams = state.teams,
                onSort = onSort,
                sortField = state.sortField,
                isDescending = state.isDescending
            )
        }

        is TeamDetailUiState.Error -> {
            with (uiState as TeamDetailUiState.Error) {
                Text(text = message)
            }
        }

        is TeamDetailUiState.Loading -> {
            with (uiState as TeamDetailUiState.Loading) {
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
    onSort: (SortField) -> Unit,
    sortField: SortField,
    isDescending : Boolean
) {
    LazyColumn {
        stickyHeader {
            TeamDetailHeader(
                onSort = onSort,
                sortField = sortField,
                isDescending = isDescending
            )
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
    onSort: (SortField) -> Unit,
    sortField: SortField,
    isDescending : Boolean
) {

    TeamDetailListItem(
        name = stringResource(id = R.string.team_detail_header_name),
        winsAgainst = stringResource(id = R.string.team_detail_header_wins),
        lossesAgainst = stringResource(id = R.string.team_detail_header_losses),
        drawsAgainst = stringResource(id = R.string.team_detail_header_draws),
        totalGames = stringResource(id = R.string.team_detail_header_total_games),
        bgColor = Color.Gray,
        clickable = true,
        onClick = onSort,
        sortField = sortField,
        isDescending = isDescending
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
    onClick: (SortField) -> Unit = {},
    sortField: SortField? = null,
    isDescending : Boolean = false
) {
    val resId = if (isDescending) R.drawable.south else R.drawable.north
    val description = if (isDescending) R.string.descending else R.string.ascending

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = bgColor),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TeamDetailsCell(
            modifier = Modifier
                .clickable(enabled = clickable) { onClick(SortField.NAME) }
                .padding(16.dp)
                .weight(1f),
            text = name,
            iconResId = if (sortField == SortField.NAME) resId else null,
            description = description
        )
        TeamDetailsCell(
            modifier = Modifier
                .clickable(enabled = clickable) { onClick(SortField.WINS) }
                .padding(16.dp)
                .defaultMinSize(minWidth = 20.dp),
            text = winsAgainst,
            iconResId = if (sortField == SortField.WINS) resId else null,
            description = description
        )
        TeamDetailsCell(
            modifier = Modifier
                .clickable(enabled = clickable) { onClick(SortField.LOSSES) }
                .padding(16.dp)
                .defaultMinSize(minWidth = 20.dp),
            text = lossesAgainst,
            iconResId = if (sortField == SortField.LOSSES) resId else null,
            description = description
        )
        TeamDetailsCell(
            modifier = Modifier
                .clickable(enabled = clickable) { onClick(SortField.DRAWS) }
                .padding(16.dp)
                .defaultMinSize(minWidth = 20.dp),
            text = drawsAgainst,
            iconResId = if (sortField == SortField.DRAWS) resId else null,
            description = description
        )
        TeamDetailsCell(
            modifier = Modifier
                .clickable(enabled = clickable) { onClick(SortField.GAMES) }
                .padding(16.dp)
                .defaultMinSize(minWidth = 20.dp),
            text = totalGames,
            iconResId = if (sortField == SortField.GAMES) resId else null,
            description = description
        )
    }
}

@Composable
fun TeamDetailsCell(
    modifier: Modifier,
    text: String,
    iconResId: Int? = null,
    description: Int? = null
) {
    Row(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier,
            text = text,
            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
        )
        iconResId?.let {
            Image(
                modifier = Modifier.padding(horizontal = 4.dp),
                painter = painterResource(id = iconResId),
                contentDescription = if (description == null) null else stringResource(id = description)
            )
        }
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
            onSort = {},
            sortField = SortField.WINS,
            isDescending = true
        )
    }
}