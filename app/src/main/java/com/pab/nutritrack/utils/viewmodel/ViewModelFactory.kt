package com.pab.nutritrack.utils.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pab.nutritrack.api.local.NutriTrackDatabase
import com.pab.nutritrack.ui.form.result.QuestionResultViewModel
import com.pab.nutritrack.ui.fragment.alarm.AlarmViewModel
import com.pab.nutritrack.ui.fragment.home.HomeViewModel
import com.pab.nutritrack.ui.fragment.recipe.RecipeViewModel
import com.pab.nutritrack.ui.fragment.setting.SettingViewModel
import com.pab.nutritrack.ui.landingpage.LandingPageViewModel
import com.pab.nutritrack.ui.rasio.ProgressRasioViewModel
import com.pab.nutritrack.ui.signuser.SignUserViewModel
import com.pab.nutritrack.utils.UserPreferences

class ViewModelFactory(private val userPreferences: UserPreferences, private val nutriTrackDatabase: NutriTrackDatabase) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context), NutriTrackDatabase.getDatabase(context))
            }.also { instance = it }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LandingPageViewModel::class.java) -> {
                LandingPageViewModel(userPreferences) as T
            }
            modelClass.isAssignableFrom(SignUserViewModel::class.java) -> {
                SignUserViewModel(userPreferences) as T
            }
            modelClass.isAssignableFrom(QuestionResultViewModel::class.java) -> {
                QuestionResultViewModel(userPreferences) as T
            }
            modelClass.isAssignableFrom(RecipeViewModel::class.java) -> {
                RecipeViewModel(userPreferences) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(userPreferences) as T
            }
            modelClass.isAssignableFrom(ProgressRasioViewModel::class.java) -> {
                ProgressRasioViewModel(userPreferences) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(userPreferences) as T
            }
            modelClass.isAssignableFrom(AlarmViewModel::class.java) -> {
                AlarmViewModel(nutriTrackDatabase) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}