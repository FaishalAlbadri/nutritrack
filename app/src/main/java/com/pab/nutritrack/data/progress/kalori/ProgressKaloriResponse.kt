package com.pab.nutritrack.data.progress.kalori

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProgressKaloriResponse(

	@field:SerializedName("progress_kalori")
	val progressKalori: List<ProgressKaloriItem>,

	@field:SerializedName("msg")
	val msg: String
) : Parcelable

@Parcelize
data class ProgressKaloriItem(

	@field:SerializedName("target_karbo")
	val targetKarbo: String,

	@field:SerializedName("target_protein")
	val targetProtein: String,

	@field:SerializedName("progress_karbo")
	val progressKarbo: String,

	@field:SerializedName("progress")
	val progress: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("id_user")
	val idUser: String,

	@field:SerializedName("target_lemak")
	val targetLemak: String,

	@field:SerializedName("create_at")
	val createAt: String,

	@field:SerializedName("progress_lemak")
	val progressLemak: String,

	@field:SerializedName("target")
	val target: String,

	@field:SerializedName("progress_protein")
	val progressProtein: String
) : Parcelable
