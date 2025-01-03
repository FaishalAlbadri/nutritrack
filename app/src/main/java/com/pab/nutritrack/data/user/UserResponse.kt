package com.pab.nutritrack.data.user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UserResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("user")
	val user: List<UserItem>
)

@Parcelize
data class UserItem(

	@field:SerializedName("goals")
	val goals: String,

	@field:SerializedName("user_birthday")
	val userBirthday: String,

	@field:SerializedName("user_password")
	val userPassword: String,

	@field:SerializedName("user_name")
	val userName: String,

	@field:SerializedName("user_weight")
	val userWeight: String,

	@field:SerializedName("user_gender")
	val userGender: String,

	@field:SerializedName("user_height")
	val userHeight: String,

	@field:SerializedName("id_user")
	val idUser: String,

	@field:SerializedName("create_at")
	val createAt: String
): Parcelable
