package cz.michalbelohoubek.interviewshowcase.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import cz.michalbelohoubek.interviewshowcase.core.model.UiMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSettingsRepositoryImpl @Inject constructor(
    private val context: Context
) : UserSettingsRepository {


    private val Context.dataStore by preferencesDataStore(
        name = "ui_mode_preferences"
    )

    override val uiMode: Flow<UiMode>
        get() = context.dataStore.data
            .map { preferences ->
                when (preferences[UserPreferencesKeys.DARK_MODE]) {
                    true -> UiMode.DARK
                    false -> UiMode.LIGHT
                    null -> UiMode.DEFAULT
                }
            }

    override suspend fun setDarkMode(switchedOn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.DARK_MODE] = switchedOn
        }
    }
}

object UserPreferencesKeys {
    val DARK_MODE = booleanPreferencesKey("DARK_MODE")
}