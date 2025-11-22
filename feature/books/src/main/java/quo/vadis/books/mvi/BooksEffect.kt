package quo.vadis.books.mvi

sealed interface BooksEffect {
    object NavigateToMain : BooksEffect
    data class ShowMessage(val message: String) : BooksEffect
}