package com.pab.nutritrack.api.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pab.nutritrack.data.AlarmData

@Dao
interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarmData: AlarmData)

    @Query("SELECT * FROM alarm ORDER BY id DESC")
    suspend fun getAlarm(): List<AlarmData>

    @Query("DELETE FROM alarm WHERE id = :id")
    suspend fun deleteAlarm(id: Int)

    @Query("UPDATE alarm SET title = :title, hour = :hour, minutes = :minutes, senin = :senin, selasa = :selasa, rabu = :rabu, kamis = :kamis, jumat = :jumat, sabtu = :sabtu, minggu = :minggu WHERE id = :id")
    suspend fun updateAlarm(id: Int, title: String, hour: Int, minutes: Int, senin: Boolean, selasa: Boolean, rabu: Boolean, kamis: Boolean, jumat: Boolean, sabtu: Boolean, minggu: Boolean)

    @Query("UPDATE alarm SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Int, status: Boolean)
}

