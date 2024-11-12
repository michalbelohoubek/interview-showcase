package cz.michalbelohoubek.interviewshowcase.core.model

enum class StageProgress {
    NOT_READY_YET, IN_PROGRESS, EARLY_FINISHED, FINISHED;

    companion object {
        val StageProgress?.orDefault: StageProgress
            get() = this ?: IN_PROGRESS

        val StageProgress.isInProgress: Boolean
            get() = this == IN_PROGRESS

        val StageProgress.isFinished: Boolean
            get() = this == FINISHED

        val StageProgress.isNotReady: Boolean
            get() = this == NOT_READY_YET
    }
}