package cz.michalbelohoubek.interviewshowcase.common.analytics

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.BuildConfig
import com.google.firebase.ktx.Firebase
import com.mixpanel.android.mpmetrics.MixpanelAPI
import cz.michalbelohoubek.interviewshowcase.common.R
import cz.michalbelohoubek.interviewshowcase.common.analytics.EventParamsEnum.Companion.asFirebaseParam
import cz.michalbelohoubek.interviewshowcase.common.analytics.EventPropertyEnum.Companion.asFirebaseEvent
import org.json.JSONObject

@SuppressLint("StaticFieldLeak")
object AnalyticsUtil {

    private val firebaseAnalytics = Firebase.analytics
    private val firebaseCrashlytics = FirebaseCrashlytics.getInstance()

    private val mixpanel: MixpanelAPI =
        MixpanelAPI.getInstance(AppContextWrapper.appContext, AppContextWrapper.appContext.getString(R.string.mixpanel_token), true)

    fun logEvent(event: EventProperty) {
        if (BuildConfig.DEBUG) {
            Log.d(event.name.asFirebaseEvent, event.params?.map { it.name.asFirebaseParam to it.value }.toString())
        }
        firebaseAnalytics.logEvent(event.name.asFirebaseEvent, Bundle().apply {
            event.params?.forEach {
                putString(it.name.asFirebaseParam, it.value)
            }
        })
        event.params?.let { params ->
            mixpanel.track(
                event.name.asFirebaseEvent,
                JSONObject().apply {
                    params.forEach {
                        put(it.name.asFirebaseParam, it.value)
                    }
                }
            )
        } ?: mixpanel.track(event.name.eventName)
    }

    fun logScreen(screenName: String) {
        firebaseAnalytics.logEvent(
            FirebaseAnalytics.Event.SCREEN_VIEW, bundleOf(
                FirebaseAnalytics.Param.SCREEN_NAME to screenName
            )
        )
        logKey(KeyEnum.SCREEN_VIEW, screenName)
        mixpanel.track(
            "screen_view",
            JSONObject().apply {
                put("name", screenName)
            }
        )
    }

    private fun logKey(key: KeyEnum, value: String) {
        firebaseCrashlytics.setCustomKey(key.name.lowercase(), value)
    }

    fun logout() {
        mixpanel.reset()
    }

    fun login(userId: String, email: String?) {
        mixpanel.identify(userId)
        email?.let {
            mixpanel.people.set("email", it)
        }
    }

    fun deleteUser() {
        mixpanel.people.deleteUser()
    }

    fun logException(exception: Throwable) {
        mixpanel.track(
            "app_exception",
            JSONObject().apply {
                put("error_message", exception.message)
            }
        )
        FirebaseCrashlytics.getInstance().recordException(exception)
    }

    fun logLoginError(source: String, detail: String, exception: Exception? = null) {
        logEvent(
            EventProperty(EventPropertyEnum.LOGIN_ERROR,
                listOf(
                    EventParam(EventParamsEnum.SOURCE, source),
                    EventParam(EventParamsEnum.DETAIL, detail),
                )
            ),
        )
        exception?.let { logException(it) }
    }

    fun logLogout(source: String) {
        logEvent(
            EventProperty(EventPropertyEnum.LOGOUT,
                listOf(EventParam(EventParamsEnum.SOURCE, source),)
            ),
        )
    }

    object Screens {

        // Sign-In
        const val SIGN_IN = "sign-in"

        // Competitions
        const val COMPETITIONS = "competitions"
    }
}

data class EventProperty(
    val name: EventPropertyEnum,
    val params: List<EventParam>? = null
)

data class EventParam(
    val name: EventParamsEnum,
    val value: String
) {

}

enum class KeyEnum {
    SCREEN_VIEW
}

enum class EventPropertyEnum(val eventName: String? = null) {
    AD_IMPRESSION,
    APP_OPEN,
    LOGIN,
    VIEW_ITEM,
    SELECT_ITEM,
    SELECT_CONTENT,

    // Custom
    START_COMPETITION_CREATION("start_competition_creation"),
    LOGOUT("logout"),
    START_ANONYMOUS_LOGIN("start_anonymous_login"),
    ANONYMOUS_LOGIN("anonymous_login"),
    START_LOGIN("start_login"),
    LOGIN_SUCCESS("login_success"),
    START_LOGIN_FROM_DRAWER("start_login_from_drawer"),
    LOGIN_FROM_DRAWER("login_from_drawer"),
    LOGIN_FROM_DRAWER_CANCELLED("login_from_drawer_cancelled"),
    LOGIN_FROM_DRAWER_ACCOUNT_COLLISION("login_from_drawer_account_collision"),
    LOGIN_FROM_DRAWER_ERROR("login_from_drawer_error"),
    APP_STATE_ERROR("app_state_error"),
    DELETE_ACCOUNT_CLICK("delete_account_click"),
    DELETED_ACCOUNT("deleted_account"),
    LOGIN_ERROR("login_error");

    companion object {
        val EventPropertyEnum.asFirebaseEvent: String
            get() = when (this) {
                SELECT_ITEM -> FirebaseAnalytics.Event.SELECT_ITEM
                SELECT_CONTENT -> FirebaseAnalytics.Event.SELECT_CONTENT
                AD_IMPRESSION -> FirebaseAnalytics.Event.AD_IMPRESSION
                APP_OPEN -> FirebaseAnalytics.Event.APP_OPEN
                LOGIN -> FirebaseAnalytics.Event.LOGIN
                VIEW_ITEM -> FirebaseAnalytics.Event.VIEW_ITEM
                else -> this.eventName.orEmpty()
            }
    }
}

enum class EventParamsEnum(val paramName: String? = null) {
    SOURCE("source"),
    DETAIL("detail"),
    COMPETITIONS_UNKNOWN_ERROR("competitions_unknown_error");

    companion object {
        val EventParamsEnum.asFirebaseParam: String
            get() = this.paramName.orEmpty()
    }
}