package com.example.fingid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fingid.ui.theme.DividerColor
import com.example.fingid.ui.theme.EditProfileBackgroundColor
import com.example.fingid.ui.theme.FinGidTheme
import com.example.fingid.ui.theme.LightGrey
import com.example.fingid.ui.theme.Red
import com.example.fingid.ui.theme.White
import com.example.fingid.utils.ThousandsRubleTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditExpenseScreen(
    navController: NavController,
    expenseId: String?
) {
    val isEditing = expenseId != null && expenseId != "new"
    val topBarTitle = "Мои расходы"

    var selectedAccount by remember { mutableStateOf("Сбербанк") }
    var selectedArticle by remember { mutableStateOf("Ремонт") }
    var amount by remember { mutableStateOf(if (isEditing) "25270" else "") }
    var date by remember { mutableStateOf(if (isEditing) "25.02.2025" else "") } // TODO: use date picker
    var time by remember { mutableStateOf(if (isEditing) "23:41" else "") }     // TODO: use time picker
    var comment by remember { mutableStateOf(if (isEditing) "Ремонт - фурнитура для дверей" else "") }

    // TODO: Загрузить данные расхода, если isEditing == true

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        topBarTitle,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = "Закрыть")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        // TODO: Handle save action
                        println("Save clicked: Account=$selectedAccount, Article=$selectedArticle, Amount=$amount, Date=$date, Time=$time, Comment=$comment")
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Сохранить", tint = LightGrey)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = EditProfileBackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            FormRowExpense(
                label = "Счет",
                value = selectedAccount,
                onClick = { /* TODO: Navigate to select account screen */ println("Select Account clicked") },
                showArrow = true
            )
            HorizontalDivider(color = DividerColor)

            FormRowExpense(
                label = "Статья",
                value = selectedArticle,
                onClick = { /* TODO: Navigate to select article screen */ println("Select Article clicked") },
                showArrow = true
            )
            HorizontalDivider(color = DividerColor)

            FormInputRowExpense(
                label = "Сумма",
                value = amount,
                onValueChange = { newValue ->
                    amount = newValue.filter { it.isDigit() }
                },
                keyboardType = KeyboardType.Number,
                visualTransformation = ThousandsRubleTransformation()
            )
            HorizontalDivider(color = DividerColor)

            FormInputRowExpense( // TODO: Заменить на DatePicker
                label = "Дата",
                value = date,
                onValueChange = { date = it },
                placeholderText = "дд.мм.гггг"
            )
            HorizontalDivider(color = DividerColor)

            FormInputRowExpense( // TODO: Заменить на TimePicker
                label = "Время",
                value = time,
                onValueChange = { time = it },
                placeholderText = "чч:мм"
            )
            HorizontalDivider(color = DividerColor)

            TextField(
                value = comment,
                onValueChange = { comment = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .height(70.dp)
                    .padding(vertical = 8.dp),
                placeholder = { Text("Комментарий") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                textStyle = MaterialTheme.typography.bodyLarge
            )
            HorizontalDivider(color = DividerColor)

            Spacer(modifier = Modifier.height(6.dp))

            Button(
                onClick = {
                    // TODO: Handle delete action
                    println("Delete expense $expenseId clicked")
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red,
                    contentColor = White
                )
            ) {
                Text("Удалить расход", style = MaterialTheme.typography.bodyLarge, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun FormRowExpense(
    label: String,
    value: String,
    onClick: (() -> Unit)? = null,
    showArrow: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .height(70.dp)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(0.6f)
        )
        if (showArrow && onClick != null) {
            Spacer(modifier = Modifier.width(20.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Выбрать $label",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun FormInputRowExpense(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: androidx.compose.ui.text.input.VisualTransformation = androidx.compose.ui.text.input.VisualTransformation.None
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(0.4f)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(0.6f),
            textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.End),
            placeholder = { Text(placeholderText, textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth()) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = visualTransformation,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddNewExpenseScreenPreview() {
    FinGidTheme {
        AddEditExpenseScreen(navController = rememberNavController(), expenseId = null)
    }
}

@Preview(showBackground = true)
@Composable
fun EditExpenseScreenPreview() {
    FinGidTheme {
        AddEditExpenseScreen(navController = rememberNavController(), expenseId = "some_id_123")
    }
}