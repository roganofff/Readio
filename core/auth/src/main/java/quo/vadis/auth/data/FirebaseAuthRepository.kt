package quo.vadis.auth.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val KEY_REMEMBER = booleanPreferencesKey("auth_remember_me")
    }

    fun currentUser() = firebaseAuth.currentUser

    suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() = firebaseAuth.signOut()

    suspend fun setRememberMe(value: Boolean) {
        dataStore.edit { prefs ->
            prefs[KEY_REMEMBER] = value
        }
    }

    fun observeRememberMe(): Flow<Boolean> =
        dataStore.data.map { prefs -> prefs[KEY_REMEMBER] ?: false }
}
