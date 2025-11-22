package quo.vadis.auth.mvi

sealed interface AuthEffect {
    object NavigateToMain : AuthEffect
    data class ShowMessage(val message: String) : AuthEffect
}