package com.pab.nutritrack.ui.rasio

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pab.nutritrack.api.remote.APIConfig
import com.pab.nutritrack.data.base.BaseResponse
import com.pab.nutritrack.data.progress.kalori.ProgressKaloriItem
import com.pab.nutritrack.data.progress.kalori.ProgressKaloriResponse
import com.pab.nutritrack.data.recipe.RecipeItem
import com.pab.nutritrack.data.recipe.RecipeResponse
import com.pab.nutritrack.utils.UserPreferences
import com.pab.nutritrack.utils.viewmodel.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProgressRasioViewModel (private val userPreferences: UserPreferences) : ViewModel() {

    companion object {
        private const val TAG = "ProgressRasioViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _progressKaloriResponse = MutableLiveData<ProgressKaloriItem>()
    val progressKaloriResponse: LiveData<ProgressKaloriItem> = _progressKaloriResponse

    private val _recipeResponse = MutableLiveData<List<RecipeItem>>()
    val recipeResponse: LiveData<List<RecipeItem>> = _recipeResponse

    private val _addProgressKalori = MutableLiveData<Event<String>>()
    val addProgressKalori: LiveData<Event<String>> = _addProgressKalori

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    fun addProgress(idFood: String, idProgressKalori: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val client = APIConfig.build().addProgressKalori(getIdUser(), idFood, idProgressKalori)
            client.enqueue(object : Callback<BaseResponse> {
                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        val baseResponse = response.body()
                        if (baseResponse!!.msg.equals("Berhasil")) {
                            _addProgressKalori.value = Event(baseResponse.msg)
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

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    _isLoading.value = false
                    _message.value = Event(t.message.toString())
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
        }
    }

    fun getProgress(times: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val client = APIConfig.build().getProgressKalori(getIdUser(), times)
            client.enqueue(object : Callback<ProgressKaloriResponse> {
                override fun onResponse(
                    call: Call<ProgressKaloriResponse>,
                    response: Response<ProgressKaloriResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        val progressKaloriResponse = response.body()
                        if (progressKaloriResponse!!.msg.equals("Berhasil")) {
                            _progressKaloriResponse.value =
                                progressKaloriResponse.progressKalori.get(0)
                        } else {
                            _message.value = Event(progressKaloriResponse.msg)
                        }
                    } else {
                        _message.value = Event(response.message().toString())
                        Log.e(
                            TAG,
                            "Failure: ${response.message()}, ${response.body()?.msg.toString()}"
                        )
                    }
                }

                override fun onFailure(call: Call<ProgressKaloriResponse>, t: Throwable) {
                    _isLoading.value = false
                    _message.value = Event(t.message.toString())
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
        }
    }

    fun getHistory(idProgressKalori: String) {
        _isLoading.value = true
        val client = APIConfig.build().getHistoryKalori(idProgressKalori)
        client.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(
                call: Call<RecipeResponse>,
                response: Response<RecipeResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    val recipeResponse = response.body()
                    if (recipeResponse!!.msg.equals("Berhasil")) {
                        _recipeResponse.value = recipeResponse.recipe
                    } else {
                        _message.value = Event(recipeResponse.msg)
                    }
                } else {
                    _message.value = Event(response.message().toString())
                    Log.e(
                        TAG,
                        "Failure: ${response.message()}, ${response.body()?.msg.toString()}"
                    )
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = Event(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    suspend fun getIdUser(): String {
        return userPreferences.getSession().first().id_user
    }
}