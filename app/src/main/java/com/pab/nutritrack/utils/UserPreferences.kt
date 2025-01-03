package com.pab.nutritrack.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pab.nutritrack.data.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val ID_USER_KEY = stringPreferencesKey("id_user")
    private val INTRO_KEY = booleanPreferencesKey("intro")
    private val STATE_KEY = booleanPreferencesKey("state")

    fun getSession(): Flow<UserData> {
        return dataStore.data.map { preferences ->
            UserData(
                preferences[ID_USER_KEY] ?: "",
                preferences[STATE_KEY] ?: false,
                preferences[INTRO_KEY] ?: false
            )
        }
    }

    suspend fun saveSession(user: UserData) {
        dataStore.edit { preferences ->
            preferences[ID_USER_KEY] = user.id_user
            preferences[STATE_KEY] = user.isLogin
            preferences[INTRO_KEY] = user.intro
        }
    }

    suspend fun editIntro(boolean: Boolean) {
        dataStore.edit { preferences ->
            preferences[INTRO_KEY] = boolean
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null
        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}