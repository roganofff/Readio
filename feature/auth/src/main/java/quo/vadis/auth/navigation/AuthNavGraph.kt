package quo.vadis.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import quo.vadis.auth.ui.AuthScreen
import quo.vadis.navigation.Route

fun NavGraphBuilder.authGraph(onNavigateToMain: () -> Unit) {
    composable(Route.Auth.route) {
        AuthScreen(onNavigateToMain = onNavigateToMain)
    }
}