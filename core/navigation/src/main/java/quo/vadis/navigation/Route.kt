package quo.vadis.navigation

import java.net.URLEncoder

sealed class Route(val route: String) {
    object Auth : Route("auth")
    object Main : Route("main")
    object BookList : Route("bookList")
    object Upload : Route("upload")
    object Profile : Route("profile")
    object Reader : Route("reader/{bookId}?title={title}") {
        fun createRoute(bookId: String, title: String? = null): String {
            val encodedTitle = title?.let { URLEncoder.encode(it, "UTF-8") } ?: ""
            return "reader/$bookId?title=$encodedTitle"
        }
    }
}