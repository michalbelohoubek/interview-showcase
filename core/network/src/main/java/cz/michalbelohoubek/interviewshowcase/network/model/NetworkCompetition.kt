package cz.michalbelohoubek.interviewshowcase.network.model

import cz.michalbelohoubek.interviewshowcase.core.model.CompetitionType
import cz.michalbelohoubek.interviewshowcase.core.model.Sport
import cz.michalbelohoubek.interviewshowcase.core.model.SportType
import java.io.Serializable

data class NetworkCompetition(
    val id: String? = null,
    val name: String? = null,
    val sport: Sport? = null,
    val sportType: SportType? = null,
    val competitionType: CompetitionType? = null,
    val createdDate: Long? = null
) : Serializable