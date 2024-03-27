package net.schnall.compose.app

import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import net.schnall.compose.R
import net.schnall.compose.app.ext.navigateToTeamDetails
import org.koin.androidx.compose.koinViewModel

enum class NavGraph(val route: String) {
    Main("mainGraph"),
}

sealed class NavScreen(
    val route: String,
    @StringRes val title: Int?,
    val hasNavigationBar: Boolean = true,
) {
    data object TeamList: NavScreen(route = "teamList", title = R.string.team_list, hasNavigationBar = true)
    data object TeamDetails: NavScreen(route = "teamDetails/{teamId}", title = null) {
        fun createRoute(teamId: String) = "teamDetails/$teamId"
    }

    companion object {
        private val routeToScreen = NavScreen::class.sealedSubclasses.associate { it.objectInstance!!.route to it.objectInstance!! }
        fun fromRoute(route: String): NavScreen? = routeToScreen[route]
    }
}

fun NavGraphBuilder.mainGraph(
    navController: NavController,
    exitApp: () -> Unit,
    showSnackbar: (String, SnackbarDuration) -> Unit,
    updateScreenName: (String) -> Unit
) {
    navigation(startDestination = NavScreen.TeamList.route, route = NavGraph.Main.route) {
        composable(NavScreen.TeamList.route) { backStackEntry ->
            TeamListScreen(
                viewModel = koinViewModel(),
                onTeamClick = { teamId, teamName ->
                    navController.navigateToTeamDetails(teamId)
                    updateScreenName(teamName)
                },
            )
        }

        composable(
            route = NavScreen.TeamDetails.route,
            arguments = listOf(navArgument("teamId") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("teamId")?.let { teamId ->
                val viewModel: TeamDetailViewModel = koinViewModel()

                TeamDetailScreen(
                    teamId = teamId,
                    viewModel = viewModel,
                    onSort = { order -> viewModel.updateOrder(order) }
                )
            }
        }

    }
}