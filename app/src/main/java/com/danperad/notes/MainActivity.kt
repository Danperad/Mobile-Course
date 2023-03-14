package com.danperad.notes

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.danperad.notes.ui.NoteCard
import com.danperad.notes.ui.NotesViewModel
import com.danperad.notes.ui.theme.NotesTheme

class MainActivity : ComponentActivity() {
    private val notesViewModel: NotesViewModel = NotesViewModel(LocalContext.current)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    MainView(notesViewModel)
                }
            }
        }
    }
}

@Composable
fun MainView(notesViewModel: NotesViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainViewNav(notesViewModel, navController) }
        composable("about") { AboutView { navController.navigate("main") } }
        composable("edit") { NoteView(notesViewModel) { navController.navigate("main") } }
    }
}

@Composable
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
fun MainViewNav(notesViewModel: NotesViewModel, navController: NavHostController) {
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
                onNavigateToAbout = { notesViewModel.deselectAllCards(); navController.navigate("about") })
        },
        floatingActionButton = {
            CreateButton {
                notesViewModel.deselectAllCards()
                navController.navigate("edit")
            }
        }) {
        Column {
            uiState.displayingNotesCards.forEach {
                if (uiState.selectedNotesCards.isNotEmpty()) {
                    NoteCardView(it, { onSelectClick(it) }, { onSelectClick(it) }, it in uiState.selectedNotesCards)
                } else {
                    NoteCardView(
                        it,
                        { notesViewModel.openCard(it); navController.navigate("edit") },
                        { onSelectClick(it) },
                        it in uiState.selectedNotesCards
                    )
                }
            }
        }
    }
}

@Composable
fun NoteCardView(noteCard: NoteCard, onClick: () -> Unit, onSelectClick: () -> Unit, isSelected: Boolean = false) {
    Box(modifier = Modifier.border(1.dp, Color.Black).clickable(onClick = onClick)) {
        Column {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(noteCard.getHeader())
                Spacer(modifier = Modifier.weight(1.0f))
                IconButton(onClick = onSelectClick) {
                    Icon(Icons.Default.Phone, contentDescription = null)
                }
            }
            Text(noteCard.getUpdated())
            Text(noteCard.getBody())
        }
    }
}

@Composable
fun TopAppBarMain(notesViewModel: NotesViewModel, onNavigateToAbout: () -> Unit) {
    val uiState = notesViewModel.uiState.collectAsState().value
    var expanded by remember { mutableStateOf(false) }
    val activity = (LocalContext.current as? Activity)
    TopAppBar {
        Text(stringResource(R.string.app_name), fontSize = 22.sp)
        Spacer(modifier = Modifier.weight(1.0f))
        if (uiState.selectedNotesCards.isEmpty()) {
            Box {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.Menu, contentDescription = stringResource(R.string.menu_helper))
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    Text(
                        stringResource(R.string.menu_item_about), modifier = Modifier.padding(3.dp)
                            .clickable(onClick = { onNavigateToAbout(); expanded = false }), fontSize = 18.sp
                    )
                    Divider()
                    Text(
                        stringResource(R.string.menu_item_exit),
                        modifier = Modifier.padding(3.dp).clickable(onClick = { activity?.finish(); expanded = false }),
                        fontSize = 18.sp
                    )
                }
            }
        } else {
            IconButton(onClick = { notesViewModel.removeSelected() }) {
                Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.main_remove_cards))
            }
            IconButton(onClick = { notesViewModel.deselectAllCards() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.main_deselect_cards))
            }
        }
    }
}

@Composable
fun CreateButton(onNavigateToCreate: () -> Unit) {
    FloatingActionButton(onClick = onNavigateToCreate) {
        Icon(Icons.Default.Create, contentDescription = stringResource(R.string.create_note_help))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NotesTheme {
        MainView(NotesViewModel(LocalContext.current))
    }
}