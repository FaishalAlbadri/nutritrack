package com.pab.nutritrack.ui.fragment.alarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pab.nutritrack.api.local.NutriTrackDatabase
import com.pab.nutritrack.data.AlarmData
import com.pab.nutritrack.utils.viewmodel.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlarmViewModel(private val nutriTrackDatabase: NutriTrackDatabase) : ViewModel() {

    companion object {
        private const val TAG = "AlarmViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _alarmData = MutableLiveData<List<AlarmData>>()
    val alarmData: LiveData<List<AlarmData>> = _alarmData

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    fun getAlarm() = viewModelScope.launch() {
        _isLoading.value = true
        try {
            _isLoading.value = false
            val alarmData = withContext(Dispatchers.IO) {
                nutriTrackDatabase.alarmDao().getAlarm()
            }
            _alarmData.value = alarmData
        } catch (e: Exception) {
            _isLoading.value = false
            e.printStackTrace()
        }
    }

    fun addAlarm(alarmData: AlarmData) = viewModelScope.launch() {
        _isLoading.value = true
        try {
            _isLoading.value = false
            nutriTrackDatabase.alarmDao().insertAlarm(alarmData)
            _message.value = Event("Berhasil")
        } catch (e: Exception) {
            _isLoading.value = false
            e.printStackTrace()
        }
    }

    fun deleteAlarm(id: Int) = viewModelScope.launch() {
        _isLoading.value = true
        try {
            _isLoading.value = false
            nutriTrackDatabase.alarmDao().deleteAlarm(id)
            _message.value = Event("Berhasil")
        } catch (e: Exception) {
            _isLoading.value = false
            e.printStackTrace()
        }
    }

    fun editAlarm(
        id: Int,
        title: String,
        hour: Int,
        minutes: Int,
        senin: Boolean,
        selasa: Boolean,
        rabu: Boolean,
        kamis: Boolean,
        jumat: Boolean,
        sabtu: Boolean,
        minggu: Boolean
    ) = viewModelScope.launch() {
        _isLoading.value = true
        try {
            _isLoading.value = false
            nutriTrackDatabase.alarmDao().updateAlarm(
                id,
                title,
                hour,
                minutes,
                senin,
                selasa,
                rabu,
                kamis,
                jumat,
                sabtu,
                minggu
            )
            _message.value = Event("Berhasil")
        } catch (e: Exception) {
            _isLoading.value = false
            e.printStackTrace()
        }
    }

    fun updateStatus(id: Int, boolean: Boolean) = viewModelScope.launch() {
        _isLoading.value = true
        try {
            _isLoading.value = false
            nutriTrackDatabase.alarmDao().updateStatus(id, boolean)
        } catch (e: Exception) {
            _isLoading.value = false
            e.printStackTrace()
        }
    }
}