package cz.michalbelohoubek.interviewshowcase.data.model

import cz.michalbelohoubek.interviewshowcase.core.model.Competition
import cz.michalbelohoubek.interviewshowcase.core.model.CompetitionType.Companion.orDefault
import cz.michalbelohoubek.interviewshowcase.core.model.Sport.Companion.orDefault
import cz.michalbelohoubek.interviewshowcase.core.model.SportType.Companion.orDefault
import cz.michalbelohoubek.interviewshowcase.network.model.NetworkCompetition
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

val NetworkCompetition.asModel: Competition
    get() = Competition(
        id = id.orEmpty(),
        name = name.orEmpty(),
        sport = sport.orDefault,
        sportType= sportType.orDefault,
        type = competitionType.orDefault,
        createdDate = ZonedDateTime.ofInstant(Instant.ofEpochMilli(createdDate ?: 0), ZoneId.systemDefault())
    )

val Competition.asNetworkModel: NetworkCompetition
    get() = NetworkCompetition(
        id = id,
        name = name,
        sport = sport,
        sportType= sportType,
        competitionType = type,
        createdDate = createdDate.toInstant().toEpochMilli()
    )
