package com.pab.nutritrack.ui.fragment.setting

import android.content.Context
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SettingViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    companion object {
        private const val TAG = "SettingViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userResponse = MutableLiveData<UserItem>()
    val userResponse: LiveData<UserItem> = _userResponse

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    fun getUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            val client = APIConfig.build().getUser(getIdUser())
            client.enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
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
    }

    fun changeUsername(username: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val client = APIConfig.build().changeUsername(username, getIdUser())
            client.enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
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
    }

    suspend fun getIdUser(): String {
        return userPreferences.getSession().first().id_user
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.logout()
        }
    }

    fun deleteCache(context: Context) {
        try {
            val dir = context.cacheDir
            deleteDir(dir)
        } catch (e: Exception) {
        }
    }

    private fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            dir.delete()
        } else if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }
    }

}