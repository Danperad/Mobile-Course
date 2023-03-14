package com.danperad.notes.ui

import com.danperad.notes.models.Note

class NoteConverter(private val notes: List<Note>) {
    fun convertToCard(): List<NoteCard> {
        return notes.map { NoteCard(it) }
    }
}