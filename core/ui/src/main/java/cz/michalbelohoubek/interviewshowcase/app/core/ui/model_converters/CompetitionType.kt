package cz.michalbelohoubek.interviewshowcase.app.core.ui.model_converters

import androidx.annotation.StringRes
import cz.michalbelohoubek.interviewshowcase.app.core.ui.R
import cz.michalbelohoubek.interviewshowcase.core.model.CompetitionType

@StringRes
fun CompetitionType.toReadable(): Int = when(this) {
    CompetitionType.LEAGUE -> R.string.competition_type_league
    CompetitionType.LEAGUE_WITH_PLAYOFF -> R.string.competition_type_league_with_playoff
    CompetitionType.PLAYOFF -> R.string.competition_type_playoff
    CompetitionType.GROUPS_WITH_PLAYOFF -> R.string.competition_type_groups_with_playoff
}