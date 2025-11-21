package quo.vadis.auth.mvi

sealed interface AuthIntent {
    data class EmailChanged(val email: String) : AuthIntent
    data class PasswordChanged(val password: String) : AuthIntent
    data class ToggleRememberMe(val remember: Boolean) : AuthIntent
    object SubmitSignIn : AuthIntent
    object SubmitSignUp : AuthIntent
    object Retry : AuthIntent
    object CheckAutoLogin : AuthIntent
}