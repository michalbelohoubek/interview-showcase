package cz.michalbelohoubek.interviewshowcase.webview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import cz.michalbelohoubek.interviewshowcase.webview.navigation.WebViewDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val url: String = savedStateHandle.toRoute<WebViewDestination>().url

}