package com.danperad.timekiller

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        boredViewModel.cleanResult()
    })
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar() },
        floatingActionButton = { SearchButton(boredViewModel) }) {
        Column(modifier = Modifier.padding(it).padding(5.dp)) {
            Box(modifier = Modifier.border(BorderStroke(1.dp, Color.Gray)).padding(5.dp)) {
                Column {
                    Text(stringResource(R.string.search_label), fontSize = 20.sp, fontWeight = FontWeight.Medium)
                    TypeSelector(boredViewModel)
                    AccessibilitySelector(boredViewModel)
                    ParticipantsSelector(boredViewModel)
                    PriceSelector(boredViewModel)
                }
            }
            Box(modifier = Modifier.fillMaxWidth().height(80.dp).border(BorderStroke(1.dp, Color.Gray)).padding(5.dp)) {
                Column {
                    Text(stringResource(R.string.result_label), fontSize = 20.sp, fontWeight = FontWeight.Medium)
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
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = stringResource(R.string.search_type))
        Spacer(Modifier.width(10.dp))
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
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(stringResource(R.string.search_accessibility))
        Spacer(Modifier.width(10.dp))
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
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = stringResource(R.string.search_participants))
        Spacer(Modifier.width(10.dp))
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
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = stringResource(R.string.search_price))
        Spacer(Modifier.width(10.dp))
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
        Text(
            stringResource(R.string.app_name),
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(10.dp, 0.dp)
        )
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