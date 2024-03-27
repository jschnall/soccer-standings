package net.schnall.compose.data


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Game (
    @PrimaryKey
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