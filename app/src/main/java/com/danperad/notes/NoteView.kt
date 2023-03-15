package com.danperad.notes

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danperad.notes.models.Note
import com.danperad.notes.ui.NotesViewModel
import com.danperad.notes.ui.theme.NotesTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun NoteView(notesViewModel: NotesViewModel, closeView: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val uiState = notesViewModel.uiState.collectAsState().value
    val note = if (uiState.openedNoteCard != null) uiState.openedNoteCard.getNote() else Note()
    val scaffoldState = rememberScaffoldState()
    val header = remember { mutableStateOf(note.header) }
    val body = remember { mutableStateOf(note.body) }
    Scaffold(scaffoldState = scaffoldState, topBar = {
        NoteTopBar(onSaveClick = {
            coroutineScope.launch(Dispatchers.IO) {
                notesViewModel.saveCard(Note(note.id, header.value, body.value))
            }
            closeView()
        }, onCloseClick = {
            coroutineScope.launch(Dispatchers.IO) {
                notesViewModel.refreshNoteCardList()
            }
            closeView()
        })
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            TextField(
                value = header.value,
                onValueChange = { newText -> header.value = newText },
                placeholder = {
                    Text(
                        stringResource(R.string.note_title_placeholder)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = body.value,
                onValueChange = { newText -> body.value = newText },
                placeholder = {
                    Text(
                        stringResource(R.string.note_body_placeholder)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
fun NoteTopBar(onSaveClick: () -> Unit, onCloseClick: () -> Unit) {
    TopAppBar {
        Text(stringResource(R.string.note_topbar_title))
        Spacer(modifier = Modifier.weight(1.0f))
        Button(
            onClick = onSaveClick,
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            elevation = ButtonDefaults.elevation(0.dp, 0.dp)
        ) {
            Text(stringResource(R.string.note_save), color = MaterialTheme.typography.body1.color)
        }
        Button(
            onClick = onCloseClick,
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            elevation = ButtonDefaults.elevation(0.dp, 0.dp)
        ) {
            Text(stringResource(R.string.note_close), color = MaterialTheme.typography.body1.color)
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NoteViewPreview() {
    NotesTheme {
        NoteView(NotesViewModel(LocalContext.current)) {}
    }
}