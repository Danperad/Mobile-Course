package com.danperad.notes.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.danperad.notes.models.AppDatabase
import com.danperad.notes.models.Note
import com.danperad.notes.models.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NotesViewModel(appContext: Context) : ViewModel() {
    private val repository: NoteRepository
    private val _uiState = MutableStateFlow(NotesUiState(emptyList()))
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()


    init {
        val db = AppDatabase.getDataBase(appContext).notesDao()
        repository = NoteRepository(db)
    }

    fun refreshNoteCardList() {
        _uiState.update { NotesUiState(repository.getAllNotes()) }
    }

    fun saveCard(note: Note) {
        if (note.id == 0)
            repository.insertNotes(note)
        else
            repository.updateNotes(note)
        refreshNoteCardList()
    }

    fun openCard(noteCard: NoteCard?) {
        _uiState.update { NotesUiState(it.displayingNotesCards, emptyList(), noteCard) }
    }

    fun selectCard(noteCard: NoteCard) {
        val selectedList = mutableListOf(noteCard)
        _uiState.value.selectedNotesCards.forEach { selectedList.add(it) }
        _uiState.update { NotesUiState(it.displayingNotesCards, selectedList, null) }
    }

    fun deselectCard(noteCard: NoteCard) {
        val selectedList = mutableListOf<NoteCard>()
        _uiState.value.selectedNotesCards.forEach { if (it != noteCard) selectedList.add(it) }
        _uiState.update { NotesUiState(it.displayingNotesCards, selectedList, null) }
    }

    fun deselectAllCards() {
        _uiState.update { NotesUiState(it.displayingNotesCards, emptyList(), null) }
    }

    fun removeSelected() {
        _uiState.value.selectedNotesCards.forEach { repository.deleteNotes(it.getNote()) }
        refreshNoteCardList()
    }
}