package cz.michalbelohoubek.interviewshowcase.common.utils

fun Boolean?.orFalse(): Boolean =
    this ?: false

fun Boolean?.orTrue(): Boolean =
    this ?: true