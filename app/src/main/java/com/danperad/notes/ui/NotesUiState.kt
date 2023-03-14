package com.danperad.notes.ui

import com.danperad.notes.models.Note

data class NotesUiState(
    val displayingNotesCards: List<NoteCard>,
    val selectedNotesCards: List<NoteCard>,
    val openedNoteCard: NoteCard?
) {
    constructor(notes: List<Note>) : this(NoteConverter(notes).convertToCard(), emptyList(), null)
}