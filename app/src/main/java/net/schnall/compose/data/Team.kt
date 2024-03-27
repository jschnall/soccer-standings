package net.schnall.compose.data

data class TeamItem (
    val teamId: String,
    val teamName: String,
    val wins: Int,
    val losses: Int,
    val draws: Int,
    val winPercentage: Double
)

data class TeamDetailItem (
    val teamId: String,
    val teamName: String,
    val winsAgainst: Int,
    val lossesAgainst: Int,
    val drawsAgainst: Int,
    val totalGames: Int
)