package com.danperad.notes

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.danperad.notes.models.Note
import com.danperad.notes.ui.NotesViewModel

@Composable
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
fun NoteView(notesViewModel: NotesViewModel, closeView: () -> Unit) {
    val uiState = notesViewModel.uiState.collectAsState().value
    val note = if (uiState.openedNoteCard != null) uiState.openedNoteCard.getNote() else Note()
    val scaffoldState = rememberScaffoldState()
    val header = remember { mutableStateOf(note.header) }
    val body = remember { mutableStateOf(note.body) }
    Scaffold(scaffoldState = scaffoldState, topBar = {
        NoteTopBar(onSaveClick = {
            notesViewModel.saveCard(Note(note.id, header.value, body.value))
            closeView()
        }, onCloseClick = { notesViewModel.closeCard(); closeView(); })
    }) {
        Column {
            TextField(value = header.value, onValueChange = { newText -> header.value = newText }, placeholder = {
                Text(
                    stringResource(R.string.note_title_placeholder)
                )
            })
            TextField(value = body.value, onValueChange = { newText -> body.value = newText }, placeholder = {
                Text(
                    stringResource(R.string.note_body_placeholder)
                )
            })
        }
    }
}

@Composable
fun NoteTopBar(onSaveClick: () -> Unit, onCloseClick: () -> Unit) {
    TopAppBar {
        Text(stringResource(R.string.note_topbar_title))
        Spacer(modifier = Modifier.weight(1.0f))
        Button(onClick = onSaveClick) {
            Text(stringResource(R.string.note_save))
        }
        Button(onClick = onCloseClick) {
            Text(stringResource(R.string.note_close))
        }
    }
}