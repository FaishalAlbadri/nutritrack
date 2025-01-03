package com.pab.nutritrack.data.recipe

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class RecipeResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("recipe")
	val recipe: List<RecipeItem>
)

@Parcelize
data class RecipeItem(

	@field:SerializedName("food_name")
	val foodName: String,

	@field:SerializedName("food_img")
	val foodImg: String,

	@field:SerializedName("food_lemak")
	val foodLemak: String,

	@field:SerializedName("food_karbo")
	val foodKarbo: String,

	@field:SerializedName("food_kalori")
	val foodKalori: String,

	@field:SerializedName("food_desc")
	val foodDesc: String,

	@field:SerializedName("food_protein")
	val foodProtein: String,

	@field:SerializedName("create_at")
	val createAt: String,

	@field:SerializedName("id_food")
	val idFood: String
): Parcelable
