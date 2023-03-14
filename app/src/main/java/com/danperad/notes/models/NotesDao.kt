package com.danperad.notes.models

import androidx.room.*

@Dao
interface NotesDao {
    @Query("SELECT * FROM note")
    fun getAll(): List<Note>

    @Insert
    fun insert(vararg note: Note)

    @Update
    fun update(vararg note: Note)

    @Delete
    fun delete(vararg note: Note)
}