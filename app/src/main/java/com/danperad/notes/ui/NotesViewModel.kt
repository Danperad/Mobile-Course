package com.danperad.notes.ui

import android.content.Context
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import com.danperad.notes.models.AppDataBase
import com.danperad.notes.models.Note
import com.danperad.notes.models.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesViewModel(appContext: Context) : ViewModel() {
    private val repository: NoteRepository
    private val _uiState = MutableStateFlow(NotesUiState(emptyList()))
    private val coroutineScope: CoroutineScope
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()


    init {
        val db = AppDataBase.getDataBase(appContext).notesDao()
        repository = NoteRepository(db)
        coroutineScope = rememberCoroutineScope()
        refreshNoteCardList()
    }

    private fun refreshNoteCardList() {
        coroutineScope.launch {
            _uiState.update { NotesUiState(repository.getAllNotes()) }
        }
    }

    fun saveCard(note: Note) {
        coroutineScope.launch {
            if (note.id == 0)
                repository.insertNotes(note)
            else
                repository.updateNotes(note)
        }
        refreshNoteCardList()
    }

    fun openCard(noteCard: NoteCard?) {
        _uiState.update { NotesUiState(it.displayingNotesCards, emptyList(), noteCard) }
    }

    fun closeCard() {
        refreshNoteCardList()
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
        coroutineScope.launch {
            _uiState.value.selectedNotesCards.forEach { repository.deleteNotes(it.getNote()) }
        }
        refreshNoteCardList()
    }

    fun changeTheme() {
        _isDarkTheme.update { !it }
    }
}