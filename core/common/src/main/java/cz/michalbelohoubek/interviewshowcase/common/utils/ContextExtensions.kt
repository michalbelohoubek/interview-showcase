package cz.michalbelohoubek.interviewshowcase.common.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.openEmail(title: String, senderEmail: String) {
    val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", senderEmail, null))
    startActivity(Intent.createChooser(intent, title))
}

fun Context.openEmailWithSubject(title: String, senderEmail: String, subject: String) {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:$senderEmail?subject=${Uri.encode(subject)}")
    startActivity(Intent.createChooser(intent, title))
}
