package quo.vadis.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import quo.vadis.auth.data.AuthRepository
import quo.vadis.auth.mvi.AuthEffect
import quo.vadis.auth.mvi.AuthIntent
import quo.vadis.auth.mvi.AuthState
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _effects = Channel<AuthEffect>(Channel.Factory.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        viewModelScope.launch {
            repo.observeRememberMe().collect { remember ->
                _state.update { it.copy(rememberMe = remember) }
            }
        }
        onIntent(AuthIntent.CheckAutoLogin)
    }

    fun onIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.EmailChanged -> _state.update { it.copy(email = intent.email, error = null) }
            is AuthIntent.PasswordChanged -> _state.update { it.copy(password = intent.password, error = null) }
            is AuthIntent.ToggleRememberMe -> viewModelScope.launch {
                repo.setRememberMe(intent.remember)
                _state.update { it.copy(rememberMe = intent.remember) }
            }
            AuthIntent.SubmitSignIn -> attemptSignIn()
            AuthIntent.SubmitSignUp -> attemptSignUp()
            AuthIntent.CheckAutoLogin -> checkAutoLogin()
            AuthIntent.Retry -> {
                _state.update { it.copy(error = null) }
                attemptSignIn()
            }
        }
    }

    private fun checkAutoLogin() {
        viewModelScope.launch {
            val user = repo.currentUser()
            if (user != null) {
                _state.update { it.copy(isSignedIn = true) }
                _effects.send(AuthEffect.NavigateToMain)
            }
        }
    }

    private fun attemptSignIn() {
        val email = _state.value.email.trim()
        val password = _state.value.password

        val validationError = validate(email, password)
        if (validationError != null) {
            viewModelScope.launch { _effects.send(AuthEffect.ShowMessage(validationError)) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val res = repo.signIn(email, password)
                if (res.isSuccess) {
                    if (_state.value.rememberMe) repo.setRememberMe(true)
                    _state.update { it.copy(isLoading = false, isSignedIn = true) }
                    _effects.send(AuthEffect.NavigateToMain)
                } else {
                    val errMsg = res.exceptionOrNull()?.localizedMessage ?: "Неизвестная ошибка"
                    _state.update { it.copy(isLoading = false, error = errMsg) }
                    _effects.send(AuthEffect.ShowMessage(errMsg))
                }
            } catch (_: IOException) {
                val msg = "Ошибка сети. Проверьте соединение."
                _state.update { it.copy(isLoading = false, error = msg) }
                _effects.send(AuthEffect.ShowMessage(msg))
            } catch (e: Exception) {
                val msg = e.localizedMessage ?: "Ошибка авторизации"
                _state.update { it.copy(isLoading = false, error = msg) }
                _effects.send(AuthEffect.ShowMessage(msg))
            }
        }
    }

    private fun attemptSignUp() {
        val email = _state.value.email.trim()
        val password = _state.value.password

        val validationError = validate(email, password)
        if (validationError != null) {
            viewModelScope.launch { _effects.send(AuthEffect.ShowMessage(validationError)) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val res = repo.signUp(email, password)
                if (res.isSuccess) {
                    if (_state.value.rememberMe) repo.setRememberMe(true)
                    _state.update { it.copy(isLoading = false, isSignedIn = true) }
                    _effects.send(AuthEffect.NavigateToMain)
                } else {
                    val errMsg = res.exceptionOrNull()?.localizedMessage ?: "Неизвестная ошибка"
                    _state.update { it.copy(isLoading = false, error = errMsg) }
                    _effects.send(AuthEffect.ShowMessage(errMsg))
                }
            } catch (e: IOException) {
                val msg = "Ошибка сети. Проверьте соединение."
                _state.update { it.copy(isLoading = false, error = msg) }
                _effects.send(AuthEffect.ShowMessage(msg))
            } catch (e: Exception) {
                val msg = e.localizedMessage ?: "Ошибка регистрации"
                _state.update { it.copy(isLoading = false, error = msg) }
                _effects.send(AuthEffect.ShowMessage(msg))
            }
        }
    }

    private fun validate(email: String, password: String): String? {
        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Введите корректный email"
        }
        if (password.length < 6) {
            return "Пароль должен содержать минимум 6 символов"
        }
        return null
    }
}