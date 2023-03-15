package com.danperad.notes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danperad.notes.ui.theme.NotesTheme

@Composable
fun AboutView(onExit: () -> Unit) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(scaffoldState = scaffoldState, topBar = { AboutTopBar(onExit) }) {
        Column(modifier = Modifier.padding(it)) {
            Text(stringResource(R.string.about_developer), fontSize = 20.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text(stringResource(R.string.about_copyright))
        }
    }
}

@Composable
fun AboutTopBar(onClick: () -> Unit){
    TopAppBar{
        Text(stringResource(R.string.about_topbar_title))
        Spacer(modifier = Modifier.weight(1.0f))
        IconButton(onClick = onClick){
            Icon(Icons.Default.ArrowBack, contentDescription = null)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AboutPreview(){
    NotesTheme {
        AboutView { }
    }
}