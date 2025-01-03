package com.pab.nutritrack.ui.landingpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.pab.nutritrack.data.UserData
import com.pab.nutritrack.utils.UserPreferences

class LandingPageViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    fun getSessionUser(): LiveData<UserData> {
        return userPreferences.getSession().asLiveData()
    }

}