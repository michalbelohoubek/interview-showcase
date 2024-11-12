package cz.michalbelohoubek.interviewshowcase.core.model

enum class SportType {
    TEAM, INDIVIDUAL;

    companion object {
        val SportType?.orDefault: SportType
            get() = this.takeIf { it != null } ?: TEAM

        val SportType.isTeam: Boolean
            get() = this == TEAM

        val SportType.isIndividual: Boolean
            get() = this == INDIVIDUAL
    }
}