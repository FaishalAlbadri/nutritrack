package com.pab.nutritrack.ui.fragment.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pab.nutritrack.api.APIConfig
import com.pab.nutritrack.data.recipe.RecipeItem
import com.pab.nutritrack.data.recipe.RecipeResponse
import com.pab.nutritrack.utils.UserPreferences
import com.pab.nutritrack.utils.viewmodel.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    companion object {
        private const val TAG = "RecipeViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _recipeResponse = MutableLiveData<List<RecipeItem>>()
    val recipeResponse: LiveData<List<RecipeItem>> = _recipeResponse

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    fun getRecipe() {
        _isLoading.value = true
        val client = APIConfig.build().getRecipe()
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
}