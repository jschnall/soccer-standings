package net.schnall.compose.data


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Game (
    @SerializedName("gameId")
    val gameId: String,
    @SerializedName("awayTeamId")
    val awayTeamId: String,
    @SerializedName("awayTeamName")
    val awayTeamName: String,
    @SerializedName("awayScore")
    val awayTeamScore: Int,
    @SerializedName("homeTeamId")
    val homeTeamId: String,
    @SerializedName("homeTeamName")
    val homeTeamName: String,
    @SerializedName("homeScore")
    val homeTeamScore: Int,
)