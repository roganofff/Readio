package quo.vadis.readio.presentation.navigation

import java.net.URLEncoder

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object SignUp : Screen("signUp")
    object Main : Screen("main")
    object BookList : Screen("bookList")
    object Upload : Screen("upload")
    object Profile : Screen("profile")
    object Reader : Screen("reader/{bookId}?title={title}") {
        fun createRoute(bookId: String, title: String? = null): String {
            val encodedTitle = title?.let { URLEncoder.encode(it, "UTF-8") } ?: ""
            return "reader/$bookId?title=$encodedTitle"
        }
    }
}