package cz.michalbelohoubek.interviewshowcase.core.model

enum class AuthStatus {
    LOADING,
    LOGGED_IN,
    LOGGED_OUT,
    LOGGED_ANONYMOUSLY;

    companion object {
        val AuthStatus.isAuthenticated: Boolean
            get() = this == LOGGED_IN || this == LOGGED_ANONYMOUSLY

        val AuthStatus.isAnonymouslyLoggedIn: Boolean
            get() = this == LOGGED_ANONYMOUSLY

        val AuthStatus.isUserLoggedIn: Boolean
            get() = this == LOGGED_IN

        val AuthStatus.isNotSet: Boolean
            get() = this == LOADING
    }
}