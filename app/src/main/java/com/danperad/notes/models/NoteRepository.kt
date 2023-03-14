package com.danperad.notes.models

class NoteRepository(private val notesDao: NotesDao) {
    fun getAllNotes(): List<Note> = notesDao.getAll()
    fun insertNotes(vararg note: Note) = notesDao.insert(*note)
    fun updateNotes(vararg note: Note) = notesDao.update(*note)
    fun deleteNotes(vararg note: Note) = notesDao.delete(*note)
}