package com.pab.nutritrack.utils.viewmodel

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.pab.nutritrack.utils.UserPreferences

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("token")

object Injection {
    fun provideRepository(context: Context): UserPreferences {
        return UserPreferences.getInstance(context.dataStore)
    }
}