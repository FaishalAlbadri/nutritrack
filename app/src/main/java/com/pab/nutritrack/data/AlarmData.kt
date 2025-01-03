package com.pab.nutritrack.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "alarm")
data class AlarmData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val hour: Int,
    val minutes: Int,
    val senin: Boolean,
    val selasa: Boolean,
    val rabu: Boolean,
    val kamis: Boolean,
    val jumat: Boolean,
    val sabtu: Boolean,
    val minggu: Boolean,
    val status: Boolean,
) : Parcelable