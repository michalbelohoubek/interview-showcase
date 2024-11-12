package cz.michalbelohoubek.interviewshowcase.app.core.ui.states

sealed class UiError {
    data class Res(val id: Int) : UiError()
    data class Plain(val string: String) : UiError()
}