package com.pab.nutritrack.ui.signuser

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pab.nutritrack.api.APIConfig
import com.pab.nutritrack.data.UserData
import com.pab.nutritrack.data.user.UserItem
import com.pab.nutritrack.data.user.UserResponse
import com.pab.nutritrack.utils.UserPreferences
import com.pab.nutritrack.utils.viewmodel.Event
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUserViewModel (private val userPreferences: UserPreferences) : ViewModel() {

    companion object {
        private const val TAG = "SignUserViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userResponse = MutableLiveData<UserItem>()
    val userResponse: LiveData<UserItem> = _userResponse

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    fun signin(username: String, password: String) {
        _isLoading.value = true
        val client = APIConfig.build().userSignIn(username, password)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    val userResponse = response.body()
                    if (userResponse!!.msg.equals("Berhasil")) {
                        _userResponse.value = userResponse.user.get(0)
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

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = Event(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun signup(username: String, password: String) {
        _isLoading.value = true
        val client = APIConfig.build().userSignUp(username, password)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    val userResponse = response.body()
                    _message.value = Event(userResponse!!.msg)
                } else {
                    _message.value = Event(response.message().toString())
                    Log.e(
                        TAG,
                        "Failure: ${response.message()}, ${response.body()?.msg.toString()}"
                    )
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = Event(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun saveSession(userData: UserData) {
        viewModelScope.launch {
            userPreferences.saveSession(userData)
        }
    }
}