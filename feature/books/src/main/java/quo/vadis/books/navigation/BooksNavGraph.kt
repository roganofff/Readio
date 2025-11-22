package quo.vadis.books.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import quo.vadis.books.ui.PlaceholderBooksScreen
import quo.vadis.navigation.Route

fun NavGraphBuilder.booksGraph(onOpenReader: (String, String?) -> Unit) {
    composable(Route.BookList.route) {
        PlaceholderBooksScreen(onBookClick = { id, title -> onOpenReader(id, title) })
    }
}