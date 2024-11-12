package cz.michalbelohoubek.interviewshowcase.data.repository

import cz.michalbelohoubek.interviewshowcase.core.model.UiMode
import kotlinx.coroutines.flow.Flow

interface UserSettingsRepository {

    val uiMode: Flow<UiMode>

    suspend fun setDarkMode(switchedOn: Boolean)
}