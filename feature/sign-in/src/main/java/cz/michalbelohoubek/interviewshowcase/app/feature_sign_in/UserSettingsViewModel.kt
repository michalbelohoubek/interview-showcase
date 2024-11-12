package cz.michalbelohoubek.interviewshowcase.app.feature_sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.michalbelohoubek.interviewshowcase.data.repository.UserSettingsRepository
import cz.michalbelohoubek.interviewshowcase.core.model.UiMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSettingsViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    val uiMode: StateFlow<UiMode> =
        userSettingsRepository
            .uiMode
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = UiMode.NOT_SET
            )

    fun setUiMode(isDarkMode: Boolean) = viewModelScope.launch {
        userSettingsRepository.setDarkMode(isDarkMode)
    }
}