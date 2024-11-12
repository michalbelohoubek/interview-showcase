package cz.michalbelohoubek.interviewshowcase.core.model

enum class UiMode {
    DEFAULT, DARK, LIGHT, NOT_SET;

    companion object {
        val UiMode?.isDarkMode: Boolean
            get() = this == DARK

        val UiMode?.isDefault: Boolean
            get() = this == DEFAULT

        val UiMode?.isNotSet: Boolean
            get() = this == NOT_SET
    }
}