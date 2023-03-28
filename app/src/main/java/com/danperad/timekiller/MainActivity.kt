package com.danperad.timekiller

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.danperad.timekiller.models.PARTICIPANTS
import com.danperad.timekiller.models.TYPES
import com.danperad.timekiller.ui.BoredViewModel
import com.danperad.timekiller.ui.theme.TimeKillerTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val boredViewModel = BoredViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimeKillerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainView(boredViewModel)
                }
            }
        }
    }
}

@Composable
fun MainView(boredViewModel: BoredViewModel) {
    val uiState = boredViewModel.uiState.collectAsState().value
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = uiState.activity?.errMsg, block = {
        if (uiState.activity == null) return@LaunchedEffect
        if (uiState.activity.isSuccess) return@LaunchedEffect
        scaffoldState.snackbarHostState.showSnackbar(
            message = uiState.activity.errMsg!!,
            actionLabel = null,
            duration = SnackbarDuration.Short,
        )
    })
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar() },
        floatingActionButton = { SearchButton(boredViewModel) }) {
        Column(modifier = Modifier.padding(it)) {
            Box {
                Column {
                    Text(stringResource(R.string.search_label))
                    TypeSelector(boredViewModel)
                    AccessibilitySelector(boredViewModel)
                    ParticipantsSelector(boredViewModel)
                    PriceSelector(boredViewModel)
                }
            }
            Box {
                Column {
                    Text(stringResource(R.string.result_label))
                    Text(
                        text = if (boredViewModel.checkResult()) {
                            uiState.activity!!.finedActivity!!.activity
                        } else {
                            ""
                        }
                    )
                }

            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun TypeSelector(boredViewModel: BoredViewModel) {
    val uiState = boredViewModel.uiState.collectAsState().value
    var expanded by remember { mutableStateOf(false) }
    Row {
        Text(stringResource(R.string.search_type))
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            TextField(
                readOnly = true,
                value = uiState.activityFilter.type,
                onValueChange = {},
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                })
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                TYPES.forEach { selectOption ->
                    DropdownMenuItem(onClick = {
                        boredViewModel.changeType(selectOption)
                        expanded = false
                    }) {
                        Text(text = selectOption)
                    }
                }
            }
        }
    }
}


@Composable
fun AccessibilitySelector(boredViewModel: BoredViewModel) {
    val uiState = boredViewModel.uiState.collectAsState().value
    Row {
        Text(stringResource(R.string.search_accessibility))
        Slider(
            value = uiState.activityFilter.accessibility.toFloat(),
            valueRange = 0f..1f,
            steps = 9,
            onValueChange = {
                boredViewModel.changeAccessibility(it.toDouble())
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ParticipantsSelector(boredViewModel: BoredViewModel) {
    val uiState = boredViewModel.uiState.collectAsState().value
    var expanded by remember { mutableStateOf(false) }
    Row {
        Text(stringResource(R.string.search_participants))
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            TextField(
                readOnly = true,
                value = uiState.activityFilter.participants.toString(),
                onValueChange = {},
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                })
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                PARTICIPANTS.forEach { selectOption ->
                    DropdownMenuItem(onClick = {
                        boredViewModel.changeParticipants(selectOption)
                        expanded = false
                    }) {
                        Text(text = selectOption.toString())
                    }
                }
            }
        }
    }
}

@Composable
fun PriceSelector(boredViewModel: BoredViewModel) {
    val uiState = boredViewModel.uiState.collectAsState().value
    Row {
        Text(stringResource(R.string.search_price))
        Slider(
            value = uiState.activityFilter.price.toFloat(),
            valueRange = 0f..1f,
            steps = 9,
            onValueChange = {
                boredViewModel.changePrice(it.toDouble())
            }
        )
    }
}

@Composable
fun TopBar() {
    val activity = (LocalContext.current as? Activity)
    TopAppBar {
        Text(stringResource(R.string.app_name))
        Spacer(modifier = Modifier.weight(1.0f))
        IconButton(onClick = { activity?.finish(); }) {
            Icon(Icons.Default.ExitToApp, contentDescription = null)
        }
    }
}

@Composable
fun SearchButton(boredViewModel: BoredViewModel) {
    val coroutineScope = rememberCoroutineScope()
    FloatingActionButton(onClick = {
        coroutineScope.launch(Dispatchers.IO) {
            boredViewModel.findActivity()
        }
    }) {
        Icon(Icons.Default.Search, contentDescription = null)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val boredViewModel = BoredViewModel()
    TimeKillerTheme {
        MainView(boredViewModel)
    }
}