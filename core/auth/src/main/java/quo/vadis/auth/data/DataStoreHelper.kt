package quo.vadis.auth.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

object SettingsKeys {
    val REMEMBER_ME = booleanPreferencesKey("remember_me")
}

suspend fun Context.setRememberMe(value: Boolean) {
    this.dataStore.edit { prefs ->
        prefs[SettingsKeys.REMEMBER_ME] = value
    }
}

fun Context.getRememberMeFlow(): Flow<Boolean> =
    this.dataStore.data.map { prefs -> prefs[SettingsKeys.REMEMBER_ME] ?: true }