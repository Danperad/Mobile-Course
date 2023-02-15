package com.danperad.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danperad.calculator.models.Calculator
import com.danperad.calculator.models.OperationType
import com.danperad.calculator.ui.CalcViewModel
import com.danperad.calculator.ui.theme.MyApplicationTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val calcViewModel: CalcViewModel = CalcViewModel(Calculator())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color.Gray) {
                    MainScreen(calcViewModel)
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun MainScreen(calcViewModel: CalcViewModel) {
    val uiStateValue = calcViewModel.uiState.collectAsState().value
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val invalidNumMsg = stringResource(R.string.invalid_number_msg)
    Scaffold(scaffoldState = scaffoldState) {
        Column(modifier = Modifier.fillMaxSize()) {
            OutlinedTextField(
                uiStateValue.displayedNumber,
                {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
                    .padding(5.dp, 5.dp, 5.dp, 0.dp)
                    .border(defaultBorderStroke())
                    .background(Color.LightGray),
                textStyle = TextStyle(
                    fontSize = 30.sp,
                    color = Color.Black,
                    textAlign = TextAlign.End
                ),
                placeholder = { PlaceHolderText(stringResource(R.string.placeholder), Modifier.fillMaxWidth()) }
            )
            Row(
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 5.dp)
                    .height(IntrinsicSize.Min)
            ) {
                TextCalcButton(
                    "7",
                    Modifier.weight(5f)
                ) { calcViewModel.appendCharToCurNumber('7') }
                Spacer(modifier = Modifier.weight(0.5f))
                TextCalcButton(
                    "8",
                    Modifier.weight(5f)
                ) { calcViewModel.appendCharToCurNumber('8') }
                Spacer(modifier = Modifier.weight(0.5f))
                TextCalcButton(
                    "9",
                    Modifier.weight(5f)
                ) { calcViewModel.appendCharToCurNumber('9') }
                Spacer(modifier = Modifier.weight(0.5f))
                IconCalcButton(
                    Icons.Outlined.ArrowBack, stringResource(R.string.backspace_description),
                    Modifier
                        .weight(5f)
                        .fillMaxHeight()
                ) { calcViewModel.removeLastCharFromCurNumber() }
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 5.dp)
                    .height(IntrinsicSize.Min)
            ) {
                TextCalcButton(
                    "4",
                    Modifier.weight(5f)
                ) { calcViewModel.appendCharToCurNumber('4') }
                Spacer(modifier = Modifier.weight(0.5f))
                TextCalcButton(
                    "5",
                    Modifier.weight(5f)
                ) { calcViewModel.appendCharToCurNumber('5') }
                Spacer(modifier = Modifier.weight(0.5f))
                TextCalcButton(
                    "6",
                    Modifier.weight(5f)
                ) { calcViewModel.appendCharToCurNumber('6') }
                Spacer(modifier = Modifier.weight(0.5f))
                TextCalcButton(
                    "+",
                    Modifier.weight(5f)
                ) { calcViewModel.runOperation(OperationType.Add) }
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 5.dp)
                    .height(IntrinsicSize.Min)
            ) {
                TextCalcButton(
                    "1",
                    Modifier.weight(5f)
                ) { calcViewModel.appendCharToCurNumber('1') }
                Spacer(modifier = Modifier.weight(0.5f))
                TextCalcButton(
                    "2",
                    Modifier.weight(5f)
                ) { calcViewModel.appendCharToCurNumber('2') }
                Spacer(modifier = Modifier.weight(0.5f))
                TextCalcButton(
                    "3",
                    Modifier.weight(5f)
                ) { calcViewModel.appendCharToCurNumber('3') }
                Spacer(modifier = Modifier.weight(0.5f))
                TextCalcButton(
                    "-",
                    Modifier.weight(5f)
                ) { calcViewModel.runOperation(OperationType.Sub) }
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 5.dp)
                    .height(IntrinsicSize.Min)
            ) {
                TextCalcButton(
                    "0",
                    Modifier.weight(5f)
                ) { calcViewModel.appendCharToCurNumber('0') }
                Spacer(modifier = Modifier.weight(0.5f))
                TextCalcButton(
                    "00",
                    Modifier.weight(5f)
                ) { calcViewModel.appendStrToCurNumber("00") }
                Spacer(modifier = Modifier.weight(0.5f))
                TextCalcButton(
                    ".",
                    Modifier.weight(5f)
                ) { calcViewModel.appendCharToCurNumber('.') }
                Spacer(modifier = Modifier.weight(0.5f))
                TextCalcButton(
                    "*",
                    Modifier.weight(5f)
                ) { calcViewModel.runOperation(OperationType.Mul) }
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 5.dp)
                    .height(IntrinsicSize.Min)
            ) {
                TextCalcButton(
                    "=",
                    Modifier.weight(5f)
                ) { calcViewModel.runOperation(OperationType.Empty) }
                Spacer(modifier = Modifier.weight(0.5f))
                TextCalcButton(
                    "CE",
                    Modifier.weight(5f)
                ) { calcViewModel.resetState() }
                Spacer(modifier = Modifier.weight(0.5f))
                TextCalcButton(
                    "%",
                    Modifier.weight(5f)
                ) { calcViewModel.runOperation(OperationType.DivPart) }
                Spacer(modifier = Modifier.weight(0.5f))
                TextCalcButton(
                    "/",
                    Modifier.weight(5f)
                ) { calcViewModel.runOperation(OperationType.Div) }
            }
            if (uiStateValue.isInvalidNumber){
                coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = invalidNumMsg,
                        actionLabel = null,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }
}

@Composable
fun TextCalcButton(text: String, modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.padding(1.dp),
        border = defaultBorderStroke(),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray, contentColor = Color.Black)
    ) {
        Text(text, style = TextStyle(fontSize = 30.sp, color = Color.Black))
    }
}

@Composable
fun IconCalcButton(icon: ImageVector, helpText: String, modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.padding(1.dp),
        border = defaultBorderStroke(),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray, contentColor = Color.Black)
    ) {
        Icon(icon, contentDescription = helpText, tint = Color.Black)
    }
}

@Composable
fun PlaceHolderText(text: String, modifier: Modifier) {
    Text(
        text, modifier, style = TextStyle(fontSize = 30.sp, color = Color.Black, textAlign = TextAlign.End)
    )
}

private fun defaultBorderStroke(): BorderStroke {
    return BorderStroke(2.dp, Color.DarkGray)
}

@Preview(showBackground = true)
@Composable
fun DefaultCalcView() {
    val calcViewModel = CalcViewModel(Calculator())
    MainScreen(calcViewModel)
}