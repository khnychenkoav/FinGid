package com.example.fingid.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fingid.R
import com.example.fingid.ui.commonitems.UiState
import com.example.fingid.ui.theme.*
import com.example.fingid.utils.ThousandsRubleTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAccountScreen(
    navController: NavController,
    initialBalance: String,
    viewModel: EditAccountViewModel
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val accountDetails by viewModel.accountDetails.collectAsState()
    var accountName by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf("RUB") }

    LaunchedEffect(accountDetails, initialBalance) {
        accountDetails?.let { acc ->
            accountName = acc.name
            balance = acc.balance.toString()
            currency = acc.currency
        } ?: run {
            balance = initialBalance.replace(Regex("[^0-9.-]"), "").ifEmpty { "0" }
        }
    }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is UiState.Success -> {
                (state.data as? String)?.let {
                    if (it.contains("сохранен") || it.contains("удален")) {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        viewModel.resetState()
                        navController.popBackStack()
                    }
                }
            }
            is UiState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            is UiState.Loading -> { }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Мой счет",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, null)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.saveAccount(accountName, balance, currency)
                    }) {
                        Icon(Icons.Default.Check, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor     = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor     = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { pv ->

        Column(
            modifier = Modifier
                .padding(pv)
                .fillMaxSize()
                .background(EditProfileBackgroundColor)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().background(White).height(56.dp)
            ) {
                TextField(
                    value = accountName,
                    onValueChange = { accountName = it },
                    placeholder = { Text("Название счета") },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    )
                )
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .height(56.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = null,
                    tint   = LightGrey,
                    modifier = Modifier.padding(start = 16.dp).size(16.dp)
                )
                Spacer(Modifier.width(20.dp))
                Text(
                    text = "Баланс",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(0.35f)
                )
                TextField(
                    value = balance,
                    onValueChange = { input ->
                        balance = input.filterIndexed { idx, ch ->
                            ch.isDigit() || (ch == '.' && !input.contains('.')) || (ch == '-' && idx == 0)
                        }
                    },
                    singleLine  = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    visualTransformation = ThousandsRubleTransformation(currency),
                    textStyle   = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.End),

                    colors = TextFieldDefaults.colors(
                        focusedContainerColor   = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor  = Color.Transparent,
                        focusedIndicatorColor   = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    modifier = Modifier
                        .weight(0.45f)
                        .focusRequester(focusRequester)
                )
                Box(
                    modifier = Modifier.fillMaxHeight().width(56.dp).background(Red).clickable { balance = "" },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = "Очистить",
                        tint = White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(Modifier.height(30.dp))
            if (viewModel.accountIdToEdit != null) {
                Button(
                    onClick = {
                        viewModel.deleteAccount()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(40.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Red,
                        contentColor = White
                    ),
                    enabled = uiState !is UiState.Loading
                ) {
                    Text("Удалить счет")
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        if (viewModel.accountIdToEdit == null) {
            focusRequester.requestFocus()
            keyboard?.show()
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun EditAccountScreenPreview_New() {
    FinGidTheme(darkTheme = false) {
        val navController = rememberNavController()
        val factory = EditAccountViewModelFactory(null)
        val viewModel: EditAccountViewModel = viewModel(factory = factory)
        EditAccountScreen(
            navController = navController,
            initialBalance = "0",
            viewModel = viewModel
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun EditAccountScreenPreview_Edit() {
    FinGidTheme(darkTheme = false) {
        val navController = rememberNavController()
        val factory = EditAccountViewModelFactory(1L)
        val viewModel: EditAccountViewModel = viewModel(factory = factory)
        EditAccountScreen(
            navController = navController,
            initialBalance = "-670000",
            viewModel = viewModel
        )
    }
}