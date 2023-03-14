package com.danperad.notes.ui

import android.annotation.SuppressLint
import com.danperad.notes.models.Note
import java.text.SimpleDateFormat

data class NoteCard(private val note: Note) {
    fun getId(): Int = note.id
    fun getHeader(): String = note.header
    fun getBody():String = note.body
    @SuppressLint("SimpleDateFormat")
    fun getUpdated(): String{
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm")
        return formatter.format(note.updated)
    }
    fun getNote(): Note = note.copy()
}