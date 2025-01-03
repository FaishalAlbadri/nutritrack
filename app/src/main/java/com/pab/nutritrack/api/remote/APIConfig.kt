package com.pab.nutritrack.api.remote

import com.pab.nutritrack.data.base.BaseResponse
import com.pab.nutritrack.data.progress.kalori.ProgressKaloriResponse
import com.pab.nutritrack.data.recipe.RecipeResponse
import com.pab.nutritrack.data.user.UserResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface APIConfig {

    companion object {
        val BASE_URL = "https://nutritrack.quexp.in/"
        val URL_API = BASE_URL + "api/"
        val URL_ASSETS = BASE_URL + "assets/"
        val URL_IMAGE_RECIPE = URL_ASSETS + "food/"

        fun build(token: String = ""): APIConfig {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client: OkHttpClient = OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                    chain.proceed(request.build())
                }
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(APIConfig::class.java)
        }
    }

    /**
     * -----------------------------------  User API  -----------------------------------
     */

    @FormUrlEncoded
    @POST("user/get")
    fun getUser(
        @Field("id_user") id_user: String,
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("user/signup")
    fun userSignUp(
        @Field("user_name") user_name: String,
        @Field("user_password") user_password: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("user/signin")
    fun userSignIn(
        @Field("user_name") user_name: String,
        @Field("user_password") user_password: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("user/editPersonalData")
    fun userEditPersonalData(
        @Field("id_user") user_name: String,
        @Field("user_gender") user_gender: String,
        @Field("user_height") user_height: String,
        @Field("user_weight") user_weight: String,
        @Field("user_birthday") user_birthday: String,
        @Field("goals") goals: String,
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("user/changeUsername")
    fun changeUsername(
        @Field("user_name") user_name: String,
        @Field("id_user") id_user: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("user/changePassword")
    fun changePassword(
        @Field("id_user") id_user: String,
        @Field("oldpass") oldpass: String,
        @Field("newpass") newpass: String
    ): Call<BaseResponse>


    /**
     * -----------------------------------  Program API  -----------------------------------
     */

    @FormUrlEncoded
    @POST("program/add")
    fun addProgram(
        @Field("id_user") id_user: String,
        @Field("program") program: String
    ): Call<BaseResponse>

    /**
     * -----------------------------------  Recipe API  -----------------------------------
     */

    @GET("recipe/get")
    fun getRecipe(): Call<RecipeResponse>

    /**
     * -----------------------------------  Progress API  -----------------------------------
     */

    @FormUrlEncoded
    @POST("progress/kalori/add")
    fun addProgressKalori(
        @Field("id_user") id_user: String,
        @Field("id_food") id_food: String,
        @Field("id_progress_kalori") id_progress_kalori: String
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST("progress/kalori/get")
    fun getProgressKalori(
        @Field("id_user") id_user: String,
        @Field("times") program: String
    ): Call<ProgressKaloriResponse>

    @FormUrlEncoded
    @POST("progress/kalori/history")
    fun getHistoryKalori(
        @Field("id_progress_kalori") id_progress_kalori: String,
    ): Call<RecipeResponse>
}