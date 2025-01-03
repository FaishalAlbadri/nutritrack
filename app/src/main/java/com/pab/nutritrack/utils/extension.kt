package com.pab.nutritrack.utils

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.text.Spanned
import androidx.appcompat.app.AlertDialog
import androidx.core.content.IntentCompat
import androidx.core.text.HtmlCompat
import com.pab.nutritrack.R

fun createAlertDialog(context: Context): AlertDialog {
    return AlertDialog.Builder(context, R.style.CustomAlertDialog)
        .setCancelable(false)
        .setView(R.layout.loading)
        .create()
}

fun htmlStringFormat(context: Context, text1: String, text2: String): Spanned {
    return HtmlCompat.fromHtml(
        String.format(
            context.resources.getString(R.string.btn_login_register),
            text1,
            text2
        ), HtmlCompat.FROM_HTML_MODE_LEGACY
    )
}

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? =
    IntentCompat.getParcelableExtra(this, key, T::class.java)