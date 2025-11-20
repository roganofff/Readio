package quo.vadis.readio.presentation.ui

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import quo.vadis.readio.presentation.navigation.Screen

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    navController: NavController = rememberNavController(),
    startDestination: String = Screen.Auth.route,
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    firebaseAuth: FirebaseAuth
) {
    val bottomTabs = listOf(
        BottomNavItem(route = "books", icon = Icons.AutoMirrored.Filled.MenuBook, label = "Книги"),
        BottomNavItem(route = "upload", icon = Icons.Default.CloudUpload, label = "Загрузить"),
        BottomNavItem(route = "profile", icon = Icons.Default.Person, label = "Профиль")
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRouteFull = navBackStackEntry?.destination?.route
    val currentRoute = currentRouteFull?.substringBefore('/')

    val bottomBarVisible = currentRoute != null && bottomTabs.any { it.route == currentRoute }

    val topBarTitle = remember(currentRouteFull, navBackStackEntry) {
        when (currentRoute) {
            Screen.BookList.route -> "Мои книги"
            Screen.Upload.route -> "Загрузка книги"
            Screen.Profile.route -> "Профиль"
            Screen.Auth.route -> "Вход в аккаунт"
            Screen.SignUp.route -> "Регистрация"
            Screen.Reader.route.substringBefore('/') -> {
                val args = navBackStackEntry?.arguments
                val encodedTitle = args?.getString("title") ?: ""
                if (encodedTitle.isNotBlank()) {
                    try {
                        Uri.decode(encodedTitle)
                    } catch (_: Exception) {
                        "Чтение"
                    }
                } else {
                    "Чтение"
                }
            }

            else -> "Приложение"
        }
    }

    val showBack = navController.previousBackStackEntry != null && (currentRoute == "reader")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(topBarTitle) },
                navigationIcon = {
                    if (showBack) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                        }
                    } else null
                }
            )
        },
        bottomBar = {
            if (bottomBarVisible) {
                AppBottomBar(
                    items = bottomTabs,
                    currentRoute = currentRoute,
                    onItemSelected = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            // === AUTH graph (feature:auth should expose this) ===
            // Replace stub with: authGraph(navController) { /* onAuthSuccess nav to MAIN */ }
            composable(Screen.Auth.route) {
                // TODO: replace with features.auth.AuthScreen
                // AuthScreen(onAuthSuccess = { navController.navigate(Screen.Main.route){ popUpTo(Screen.Auth.route){ inclusive = true } } })
                PlaceholderAuthScreen {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            }
        }
    }
}

