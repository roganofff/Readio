package quo.vadis.books.mvi

data class BooksState(
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSignedIn: Boolean = false
)