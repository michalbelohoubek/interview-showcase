package cz.michalbelohoubek.interviewshowcase.core.model

import java.io.Serializable
import java.time.ZonedDateTime
import java.util.UUID

data class Competition(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val sport: Sport = Sport.FOOTBALL,
    val sportType: SportType,
    val type: CompetitionType,
    val createdDate: ZonedDateTime
) : Serializable