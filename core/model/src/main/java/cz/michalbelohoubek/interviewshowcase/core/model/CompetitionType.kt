package cz.michalbelohoubek.interviewshowcase.core.model

/**
 * [LEAGUE] can be together with other lower leagues and can promote and relegate competitors in every of the leagues during the seasons
 * [PLAYOFF] is classic play-off and there is possibility to play free count of seasons
 * [LEAGUE_WITH_PLAYOFF] is possibility to play league with following play-off with free count of seasons
 */
enum class CompetitionType {
    LEAGUE, PLAYOFF, LEAGUE_WITH_PLAYOFF, GROUPS_WITH_PLAYOFF;//, OWN; //, LEAGUES_WITH_COEFFICIENT_WITH_NEXT_STAGES

    companion object {
        val CompetitionType?.orDefault: CompetitionType
            get() = this.takeIf { it != null } ?: LEAGUE

        val CompetitionType.isLeague: Boolean get() = this == LEAGUE

        val CompetitionType.isPlayoff: Boolean get() = this == PLAYOFF

        val CompetitionType.isLeagueWithPlayoff: Boolean get() = this == LEAGUE_WITH_PLAYOFF

        val CompetitionType.isGroupsWithPlayoff: Boolean get() = this == GROUPS_WITH_PLAYOFF
    }
}