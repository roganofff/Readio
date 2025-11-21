package quo.vadis.readio.presentation.ui

import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import quo.vadis.auth.navigation.authGraph
import quo.vadis.books.navigation.booksGraph
import quo.vadis.navigation.Route

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Route.Auth.route,
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
            Route.BookList.route -> "Мои книги"
            Route.Upload.route -> "Загрузка книги"
            Route.Profile.route -> "Профиль"
            Route.Auth.route -> "Вход в аккаунт"
            Route.Reader.route.substringBefore('/') -> {
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
            authGraph {
                navController.navigate(Route.Main.route) {
                    popUpTo(Route.Auth.route) { inclusive = true }
                }
            }

            navigation(startDestination = Route.BookList.route, route = Route.Main.route) {
                booksGraph { bookId, title ->
                    navController.navigate(Route.Reader.createRoute(bookId, title))
                }
//
//                uploadGraph(navController)
//                profileGraph(navController)

                // Reader can be defined here or inside reader feature:
//                composable(
//                    route = Route.Reader.route,
//                    arguments = listOf(
//                        navArgument("bookId") { type = NavType.StringType },
//                        navArgument("title") { type = NavType.StringType; defaultValue = ""; nullable = true }
//                    )
//                ) { backStackEntry ->
//                    val bookId = backStackEntry.arguments?.getString("bookId").orEmpty()
//                    val titleEncoded = backStackEntry.arguments?.getString("title").orEmpty()
//                    val title = if (titleEncoded.isNotBlank()) Uri.decode(titleEncoded) else null
//
//                    // call reader composable from feature:reader (or inline)
//                    ReaderScreen(bookId = bookId, title = title, onBack = { navController.navigateUp() })
//                }
            }
        }
    }
}

