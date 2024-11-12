package cz.michalbelohoubek.interviewshowcase.data.util

sealed interface DownloadPolicy {
    data object Cache : DownloadPolicy
    data object Network : DownloadPolicy
    data object None : DownloadPolicy
}