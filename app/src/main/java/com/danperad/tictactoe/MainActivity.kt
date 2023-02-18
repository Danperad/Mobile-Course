package com.danperad.tictactoe

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danperad.tictactoe.models.*
import com.danperad.tictactoe.ui.CellButton
import com.danperad.tictactoe.ui.GameViewModel
import com.danperad.tictactoe.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val gameViewModel: GameViewModel = GameViewModel(Game(GameConfiguration(GameMode.ZeroComputer)))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkTheme = gameViewModel.isDarkTheme.collectAsState(initial = false)
            TicTacToeTheme(isDarkTheme.value) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen(gameViewModel, isDarkTheme.value)
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun MainScreen(gameViewModel: GameViewModel, isDarkTheme: Boolean) {
    val uiStateValue = gameViewModel.uiState.collectAsState().value
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    val gameScope: CoroutineScope = rememberCoroutineScope()
    var gameJob: Job? = null

    val nothing = stringResource(R.string.game_info_nothing)
    val zeroWinner = stringResource(R.string.game_info_zero_win)
    val crossWinner = stringResource(R.string.game_info_cross_win)

    val crossIcon = painterResource(R.drawable.cross_icon)
    val emptyIcon = painterResource(R.drawable.empty_icon)
    val zeroIcon = painterResource(R.drawable.zero_icon)

    fun getCellColor(cell: CellButton, isDarkTheme: Boolean): Color {
        if (isDarkTheme) {
            if (cell.isWinLine)
                return DarkWinnerButton
            return when (cell.cellType) {
                CellState.Empty -> EmptyButton
                CellState.Cross -> DarkCrossButton
                CellState.Zero -> DarkZeroButton
            }
        }
        if (cell.isWinLine)
            return LightWinnerButton
        return when (cell.cellType) {
            CellState.Empty -> EmptyButton
            CellState.Cross -> LightCrossButton
            CellState.Zero -> LightZeroButton
        }
    }

    fun onClick(cell: CellButton) {
        if (cell.cellType != CellState.Empty) return
        if (gameJob != null && gameJob!!.isActive) return
        gameJob = gameScope.launch {
            if (gameViewModel.getGameConfiguration().mode == GameMode.ZeroComputer) {
                gameViewModel.crossStep(cell.rowNumber, cell.columnNumber)
            } else {
                gameViewModel.zeroStep(cell.rowNumber, cell.columnNumber)
            }
        }
    }

    fun getImage(cell: CellState): Painter =
        when (cell) {
            CellState.Cross -> crossIcon
            CellState.Empty -> emptyIcon
            CellState.Zero -> zeroIcon
        }

    Scaffold(scaffoldState = scaffoldState, topBar = { TopBar(gameViewModel) }) {
        Column {
            for (rowCells in uiStateValue.cells) {
                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    for (rowCell in rowCells) {
                        Column(
                            modifier = Modifier.weight(0.9f).padding(10.dp)
                        ) {
                            CellSetup(
                                { onClick(rowCell) },
                                getImage(rowCell.cellType),
                                getCellColor(rowCell, isDarkTheme)
                            )
                        }
                    }
                }
            }
        }
        if (!gameViewModel.isGameStarted()) {
            coroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = when (uiStateValue.stateType) {
                        GameStateType.Nothing -> nothing
                        GameStateType.ZeroWinner -> zeroWinner
                        else -> crossWinner
                    }
                )
            }
        }
    }
}

@Composable
fun CellSetup(onClick: () -> Unit, icon: Painter, color: Color) {
    Button(onClick = onClick, colors = ButtonDefaults.buttonColors(backgroundColor = color)) {
        Icon(painter = icon, contentDescription = null)
    }
}

@Composable
fun TopBar(gameViewModel: GameViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val activity = (LocalContext.current as? Activity)

    TopAppBar(Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.app_header), fontSize = 22.sp)
        Spacer(modifier = Modifier.weight(1.0f))
        Box {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(Icons.Default.Menu, contentDescription = stringResource(R.string.menu_helper))
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                Text(
                    stringResource(R.string.menu_item_new_game),
                    modifier = Modifier.padding(3.dp)
                        .clickable(onClick = { gameViewModel.startNewGame(); expanded = false }), fontSize = 18.sp
                )
                Text(
                    stringResource(R.string.menu_item_change_theme),
                    modifier = Modifier.padding(3.dp)
                        .clickable(onClick = { gameViewModel.changeTheme(); expanded = false }), fontSize = 18.sp
                )
                Divider()
                Text(
                    stringResource(R.string.menu_item_exit),
                    modifier = Modifier.padding(3.dp).clickable(onClick = { activity?.finish(); expanded = false }),
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun DefaultScreen() {
    val gameViewModel = GameViewModel(Game(GameConfiguration(GameMode.ZeroComputer)))
    TicTacToeTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Gray) {
            MainScreen(gameViewModel, true)
        }
    }
}