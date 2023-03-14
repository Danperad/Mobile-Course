package com.danperad.notes.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "header") val header: String = "",
    @ColumnInfo(name = "body") val body: String = "",
    @ColumnInfo(name = "updated") val updated: Date = Date(),
)