package com.pab.nutritrack.ui.form.result

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pab.nutritrack.api.remote.APIConfig
import com.pab.nutritrack.data.QuestionData
import com.pab.nutritrack.data.base.BaseResponse
import com.pab.nutritrack.data.user.UserResponse
import com.pab.nutritrack.utils.UserPreferences
import com.pab.nutritrack.utils.viewmodel.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class QuestionResultViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    companion object {
        private const val TAG = "QuestionResultViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _resultOpenAI = MutableLiveData<Event<String>>()
    val resultOpenAI: LiveData<Event<String>> = _resultOpenAI

    private val _resultProgram = MutableLiveData<Event<String>>()
    val resultProgram: LiveData<Event<String>> = _resultProgram

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    fun openAI(data: QuestionData) {
        _isLoading.value = true
        val apiKey = "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"
        val url = "https://api.groq.com/openai/v1/chat/completions"
        val requestPrompt =
            "Hitung kebutuhan kalori untuk program ${data.tujuan} dengan format jawaban: (Konsumsi kalori = ??? kkal, Konsumsi Air = ??? ml, Gerak Langkah Harian = ??? Langkah). Data yang digunakan:" +
                    "Tanggal Lahir: ${data.tanggalLahir}," +
                    "Jenis Kelamin: ${data.jenisKelamin}," +
                    "Tinggi Badan: ${data.tinggiBadan} cm," +
                    "Berat Badan: ${data.beratBadan} kg," +
                    "Konsumsi Air: ${data.waterQuestOne}," +
                    "Banyak Olahraga: ${data.dailyQuestTwo}," +
                    "Durasi Olahraga: ${data.dailyQuestFour}," +
                    "Aktivitas Harian: ${data.dailyQuestOne}," +
                    "Pola Makan: ${data.foodQuestOne}. " +
                    "Jawaban harus berupa angka valid tanpa range, gunakan tanda koma setelah hasil perhitungan, disampaikan secara langsung tanpa deskripsi tambahan."

        Log.i("requestPrompt", requestPrompt)

        val requestBody = """
        {
            "messages": [
                {
                    "role": "user",
                    "content": "$requestPrompt"
                }
            ],
            "model": "llama3-8b-8192",
            "temperature": 0,
            "max_tokens": 1800,
            "top_p": 1,
            "stream": true
        }
        """.trimIndent()

        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("API Error", "API request failed", e)
                viewModelScope.launch {
                    _message.value = Event("API request failed: ${e.localizedMessage}")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                Log.d("API Raw Response", "Body: $body")

                if (body.isNullOrEmpty()) {
                    viewModelScope.launch {
                        _message.value = Event("Empty response from server")
                        _isLoading.value = false
                    }
                    return
                }

                try {
                    val lines = body.split("\n")
                    val fullContent = StringBuilder()

                    for (line in lines) {
                        Log.d("Streaming Line", "Processing line: $line")

                        if (line.startsWith("data: ")) {
                            val jsonPart = line.removePrefix("data: ").trim()

                            if (jsonPart == "[DONE]") {
                                continue
                            }

                            try {
                                val jsonObject = JSONObject(jsonPart)
                                val choices = jsonObject.optJSONArray("choices") ?: continue

                                for (i in 0 until choices.length()) {
                                    val delta = choices.getJSONObject(i).getJSONObject("delta")
                                    val content = delta.optString("content", "")
                                    fullContent.append(content)
                                }
                            } catch (e: Exception) {
                                Log.e("JSON Parse Error", "Error parsing JSON part: ${e.message}")
                            }
                        }
                    }


                    if (fullContent.isEmpty()) {
                        viewModelScope.launch {
                            _message.value = Event("No valid content found in server response.")
                            _isLoading.value = false
                        }
                    } else {
                        viewModelScope.launch {
                            _resultOpenAI.value = Event(fullContent.toString().replace(".", ""))
                            _isLoading.value = false
                        }
                    }

                } catch (e: Exception) {
                    Log.e("JSON Parse Error", "Error parsing JSON: ${e.message}")
                    viewModelScope.launch {
                        _message.value = Event("Error parsing server response: ${e.message}")
                        _isLoading.value = false

                    }
                }
            }
        })
    }

    fun personalData(data: QuestionData, program: String, goals: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val client = APIConfig.build().userEditPersonalData(
                getIdUser(),
                data.jenisKelamin,
                data.tinggiBadan,
                data.beratBadan,
                data.tanggalLahir,
                goals
            )

            client.enqueue(object : retrofit2.Callback<UserResponse> {
                override fun onResponse(
                    call: retrofit2.Call<UserResponse>,
                    response: retrofit2.Response<UserResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        val userResponse = response.body()
                        if (userResponse!!.msg.equals("Berhasil")) {
                            program(program)
                        } else {
                            _message.value = Event(userResponse.msg)
                        }
                    } else {
                        _message.value = Event(response.message().toString())
                        Log.e(
                            TAG,
                            "Failure: ${response.message()}, ${response.body()?.msg.toString()}"
                        )
                    }
                }

                override fun onFailure(call: retrofit2.Call<UserResponse>, t: Throwable) {
                    _isLoading.value = false
                    _message.value = Event(t.message.toString())
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
        }
    }

    fun program(program: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val client = APIConfig.build().addProgram(
                getIdUser(),
                program
            )
            client.enqueue(object : retrofit2.Callback<BaseResponse> {
                override fun onResponse(
                    call: retrofit2.Call<BaseResponse>,
                    response: retrofit2.Response<BaseResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        val baseResponse = response.body()
                        if (baseResponse!!.msg.equals("Berhasil")) {
                            _resultProgram.value = Event(baseResponse.msg)
                        } else {
                            _message.value = Event(baseResponse.msg)
                        }
                    } else {
                        _message.value = Event(response.message().toString())
                        Log.e(
                            TAG,
                            "Failure: ${response.message()}, ${response.body()?.msg.toString()}"
                        )
                    }
                }

                override fun onFailure(call: retrofit2.Call<BaseResponse>, t: Throwable) {
                    _isLoading.value = false
                    _message.value = Event(t.message.toString())
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
        }
    }

    suspend fun getIdUser(): String {
        return userPreferences.getSession().first().id_user
    }

    fun saveIntro() {
        viewModelScope.launch {
            userPreferences.editIntro(true)
        }
    }


}