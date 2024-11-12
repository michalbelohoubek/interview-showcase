package cz.michalbelohoubek.interviewshowcase.core.model

enum class Sport {
    FOOTBALL,
    HOCKEY,
    BASKETBALL;

    fun getSportId() = name

    companion object {
        val Sport?.orDefault: Sport
            get() = this.takeIf { it != null } ?: FOOTBALL
    }
}

