package com.pab.nutritrack.ui.fragment.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pab.nutritrack.api.remote.APIConfig
import com.pab.nutritrack.data.progress.kalori.ProgressKaloriItem
import com.pab.nutritrack.data.progress.kalori.ProgressKaloriResponse
import com.pab.nutritrack.utils.UserPreferences
import com.pab.nutritrack.utils.viewmodel.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _progressKaloriResponse = MutableLiveData<ProgressKaloriItem>()
    val progressKaloriResponse: LiveData<ProgressKaloriItem> = _progressKaloriResponse

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

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

    suspend fun getIdUser(): String {
        return userPreferences.getSession().first().id_user
    }
}