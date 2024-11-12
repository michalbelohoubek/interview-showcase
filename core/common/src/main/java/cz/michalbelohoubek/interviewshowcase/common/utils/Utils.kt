package cz.michalbelohoubek.interviewshowcase.common.utils

import java.util.UUID

fun generateRandomID(): String {
    return UUID.randomUUID().toString()
}