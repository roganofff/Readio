package quo.vadis.readio.presentation.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Main : Screen("main") // главный контейнер (с bottom nav)
    object BookList : Screen("bookList")
    object Upload : Screen("upload")
    object Profile : Screen("profile")
    object Reader : Screen("reader/{bookId}") {
        fun createRoute(bookId: String) = "reader/$bookId"
    }
}