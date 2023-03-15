package com.danperad.notes

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.danperad.notes.ui.NoteCard
import com.danperad.notes.ui.NotesViewModel
import com.danperad.notes.ui.theme.NotesTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var notesViewModel: NotesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            notesViewModel = NotesViewModel(LocalContext.current)
            val coroutineScope = rememberCoroutineScope()
            val navController = rememberNavController()
            LaunchedEffect(Unit){
                coroutineScope.launch(Dispatchers.IO) {
                    notesViewModel.refreshNoteCardList()
                }
            }
            NotesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavHost(navController = navController, startDestination = "main") {
                        composable("main") {
                            MainView(notesViewModel, { navController.navigate("about") }, {
                                navController.navigate("edit")
                            })
                        }
                        composable("about") { AboutView { navController.navigate("main") } }
                        composable("edit") { NoteView(notesViewModel) { navController.navigate("main") } }
                    }
                }
            }
        }
    }
}

@Composable
fun MainView(
    notesViewModel: NotesViewModel,
    onNavigateToAbout: () -> Unit,
    onNavigateToEdit: () -> Unit
) {
    val uiState = notesViewModel.uiState.collectAsState().value
    val scaffoldState = rememberScaffoldState()
    fun onSelectClick(noteCard: NoteCard) {
        if (noteCard in uiState.selectedNotesCards) {
            notesViewModel.deselectCard(noteCard)
        } else {
            notesViewModel.selectCard(noteCard)
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBarMain(
                notesViewModel,
                onNavigateToAbout = { notesViewModel.deselectAllCards(); onNavigateToAbout() })
        },
        floatingActionButton = {
            CreateButton {
                notesViewModel.deselectAllCards()
                onNavigateToEdit()
            }
        }) {
        Column(modifier = Modifier.padding(it)) {
            uiState.displayingNotesCards.forEach { card ->
                if (uiState.selectedNotesCards.isNotEmpty()) {
                    NoteCardView(
                        card,
                        { onSelectClick(card) },
                        { onSelectClick(card) },
                        card in uiState.selectedNotesCards
                    )
                } else {
                    NoteCardView(
                        card,
                        { notesViewModel.openCard(card); onNavigateToEdit() },
                        { onSelectClick(card) },
                        card in uiState.selectedNotesCards
                    )
                }
            }
        }
    }
}

@Composable
fun NoteCardView(
    noteCard: NoteCard,
    onClick: () -> Unit,
    onSelectClick: () -> Unit,
    isSelected: Boolean = false
) {
    Box(modifier = Modifier.padding(5.dp)) {
        Box(
            modifier = Modifier
                .border(1.dp, Color.Black)
                .background(if (isSelected) Color.DarkGray else Color.Transparent)
                .padding(start = 10.dp, bottom = 5.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onClick() },
                        onLongPress = { onSelectClick() }
                    )
                }
        ) {
            Column {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(noteCard.getHeader(), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
                Text(noteCard.getUpdated())
                Text(noteCard.getBody(), fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun TopAppBarMain(notesViewModel: NotesViewModel, onNavigateToAbout: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val uiState = notesViewModel.uiState.collectAsState().value
    var expanded by remember { mutableStateOf(false) }
    val activity = (LocalContext.current as? Activity)
    TopAppBar {
        Text(stringResource(R.string.app_name), fontSize = 22.sp)
        Spacer(modifier = Modifier.weight(1.0f))
        if (uiState.selectedNotesCards.isEmpty()) {
            Box {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = stringResource(R.string.menu_helper)
                    )
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    Text(
                        stringResource(R.string.menu_item_about),
                        modifier = Modifier
                            .padding(3.dp)
                            .clickable(onClick = { onNavigateToAbout(); expanded = false }),
                        fontSize = 18.sp
                    )
                    Divider()
                    Text(
                        stringResource(R.string.menu_item_exit),
                        modifier = Modifier
                            .padding(3.dp)
                            .clickable(onClick = { activity?.finish(); expanded = false }),
                        fontSize = 18.sp
                    )
                }
            }
        } else {
            IconButton(onClick = { coroutineScope.launch(Dispatchers.IO){ notesViewModel.removeSelected()} }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(R.string.main_remove_cards)
                )
            }
            IconButton(onClick = { notesViewModel.deselectAllCards() }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.main_deselect_cards)
                )
            }
        }
    }
}

@Composable
fun CreateButton(onNavigateToCreate: () -> Unit) {
    FloatingActionButton(onClick = onNavigateToCreate) {
        Icon(Icons.Default.Add, contentDescription = stringResource(R.string.create_note_help))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NotesTheme {
        MainView(NotesViewModel(LocalContext.current), {}, {})
    }
}