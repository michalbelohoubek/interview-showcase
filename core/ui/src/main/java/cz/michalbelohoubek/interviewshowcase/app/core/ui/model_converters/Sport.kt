package cz.michalbelohoubek.interviewshowcase.app.core.ui.model_converters

import cz.michalbelohoubek.interviewshowcase.app.core.ui.R
import cz.michalbelohoubek.interviewshowcase.core.model.Sport

val Sport.stringResource
    get() = when (this) {
        Sport.FOOTBALL -> R.string.sport_football
        Sport.HOCKEY -> R.string.sport_hockey
        Sport.BASKETBALL -> R.string.sport_basketball
    }

val Sport.iconResource
    get() = when (this) {
        Sport.FOOTBALL -> R.drawable.ic_football
        Sport.HOCKEY -> R.drawable.ic_hockey
        Sport.BASKETBALL -> R.drawable.ic_basket
    }

