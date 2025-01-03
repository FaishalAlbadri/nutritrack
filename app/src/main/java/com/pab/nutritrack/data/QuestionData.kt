package com.pab.nutritrack.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionData(
    val tanggalLahir: String,
    val jenisKelamin: String,
    val tinggiBadan: String,
    val beratBadan: String,
    val tujuan: String,

    val waterQuestOne: String,
    val waterQuestTwo: String,
    val waterQuestThree: String,

    val dailyQuestOne: String,
    val dailyQuestTwo: String,
    val dailyQuestThree: String,
    val dailyQuestFour: String,
    val dailyQuestFive: String,

    val foodQuestOne: String,
    val foodQuestTwo: String,
    val foodQuestThree: String,
    val foodQuestFour: String,
) : Parcelable