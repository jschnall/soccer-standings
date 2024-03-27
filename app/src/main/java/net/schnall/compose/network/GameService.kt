package net.schnall.compose.network

import net.schnall.compose.data.Game
import retrofit2.http.GET

interface GameService {
    @GET("cv/ae/default/171221/soccer_game_results.json")
    suspend fun games(): List<Game>
}
