package cz.michalbelohoubek.interviewshowcase.common.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val dispatcher: STDispatchers)

enum class STDispatchers {
    IO,
    DEFAULT,
    MAIN,
    MAIN_IMMEDIATE
}
