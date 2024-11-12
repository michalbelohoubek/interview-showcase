package cz.michalbelohoubek.interviewshowcase.common.analytics

import android.content.Context
import android.content.ContextWrapper

class AppContextWrapper(base: Context) : ContextWrapper(base) {
    companion object {
        lateinit var appContext: Context
            private set
    }

    init {
        appContext = base
    }
}