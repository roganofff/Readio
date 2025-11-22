package quo.vadis.books.mvi

sealed interface BooksIntent {
    data class EmailChanged(val email: String) : BooksIntent
    data class PasswordChanged(val password: String) : BooksIntent
    data class ToggleRememberMe(val remember: Boolean) : BooksIntent
    object SubmitSignIn : BooksIntent
    object SubmitSignUp : BooksIntent
    object Retry : BooksIntent
    object CheckAutoLogin : BooksIntent
}