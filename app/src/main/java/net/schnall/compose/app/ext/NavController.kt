package net.schnall.compose.app.ext

import androidx.navigation.NavController
import net.schnall.compose.app.NavScreen

fun NavController.navigateToTeamDetails(teamId: String) =
    navigate(route = NavScreen.TeamDetails.createRoute(teamId))