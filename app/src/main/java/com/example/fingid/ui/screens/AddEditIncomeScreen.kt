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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fingid.ui.theme.Black
import com.example.fingid.ui.theme.DividerColor
import com.example.fingid.ui.theme.EditProfileBackgroundColor
import com.example.fingid.ui.theme.FinGidTheme
import com.example.fingid.ui.theme.LightGreen
import com.example.fingid.ui.theme.LightGrey
import com.example.fingid.ui.theme.Red
import com.example.fingid.ui.theme.White
import com.example.fingid.utils.ThousandsRubleTransformation
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditIncomeScreen(
    navController: NavController,
    incomeId: String?
) {
    val isEditing = incomeId != null && incomeId != "new"
    val topBarTitle = "Мои доходы"

    var selectedAccount by remember { mutableStateOf("Сбербанк") }
    var selectedArticle by remember { mutableStateOf("Ремонт") }
    var amount by remember { mutableStateOf(if (isEditing) "25270" else "") }
    var date by remember { mutableStateOf(if (isEditing) "25.02.2025" else "") }
    var time by remember { mutableStateOf(if (isEditing) "23:41" else "") }
    var comment by remember { mutableStateOf(if (isEditing) "Ремонт - фурнитура для дверей" else "") }

    // TODO: Загрузить данные дохода, если isEditing == true

    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    val initialDateMillis = remember {
        if (isEditing) {
            try {
                LocalDate.parse("25.02.2025", DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            } catch (e: DateTimeParseException) {
                System.currentTimeMillis()
            }
        } else {
            System.currentTimeMillis()
        }
    }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis,
        initialDisplayMode = DisplayMode.Picker,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return true
            }
        }
    )
    val selectedDate = datePickerState.selectedDateMillis?.let {
        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    val initialTime = remember {
        if (isEditing) {
            try {
                LocalTime.parse("23:41", DateTimeFormatter.ofPattern("HH:mm"))
            } catch (e: DateTimeParseException) {
                LocalTime.now()
            }
        } else {
            LocalTime.now()
        }
    }
    val timePickerState = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute,
        is24Hour = true
    )
    val selectedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)


    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

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
            FormRowIncome(
                label = "Счет",
                value = selectedAccount,
                onClick = { /* TODO: Navigate to select account screen */ println("Select Account clicked") },
                showArrow = true
            )
            HorizontalDivider(color = DividerColor)

            FormRowIncome(
                label = "Статья",
                value = selectedArticle,
                onClick = { /* TODO: Navigate to select article screen */ println("Select Article clicked") },
                showArrow = true
            )
            HorizontalDivider(color = DividerColor)

            FormInputRowIncome(
                label = "Сумма",
                value = amount,
                onValueChange = { newValue ->
                    amount = newValue.filter { it.isDigit() }
                },
                keyboardType = KeyboardType.Number,
                visualTransformation = ThousandsRubleTransformation()
            )
            HorizontalDivider(color = DividerColor)

            FormRowIncome(
                label = "Дата",
                value = selectedDate?.format(dateFormatter) ?: "Выберите дату",
                onClick = { showDatePicker = true }
            )
            HorizontalDivider(color = DividerColor)

            FormRowIncome(
                label = "Время",
                value = selectedTime.format(timeFormatter),
                onClick = { showTimePicker = true }
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
                    println("Delete expense $incomeId clicked")
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
                Text("Удалить доход", style = MaterialTheme.typography.bodyLarge, fontSize = 14.sp)
            }

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    colors = DatePickerDefaults.colors(
                        containerColor = LightGreen
                    ),
                    confirmButton = {
                        TextButton(
                            onClick = { showDatePicker = false },
                            colors = ButtonDefaults.textButtonColors(contentColor = Black)
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDatePicker = false },
                            colors = ButtonDefaults.textButtonColors(contentColor = Black)
                        ) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(
                        state = datePickerState,
                        title = null,
                        headline = null,
                        colors = DatePickerDefaults.colors(
                            containerColor = LightGreen,
                            selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                            selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
                            todayDateBorderColor = Color.Transparent,
                            todayContentColor = MaterialTheme.colorScheme.primary,
                            dayContentColor = MaterialTheme.colorScheme.onSurface,
                            disabledDayContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                            weekdayContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            navigationContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            yearContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            currentYearContentColor = MaterialTheme.colorScheme.primary,
                            selectedYearContentColor = MaterialTheme.colorScheme.onPrimary,
                            selectedYearContainerColor = MaterialTheme.colorScheme.primary,
                            subheadContentColor = MaterialTheme.colorScheme.onSurfaceVariant,

                        ),
                        showModeToggle = false
                    )
                }
            }

            if (showTimePicker) {
                Dialog(onDismissRequest = { showTimePicker = false }) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Выберите время", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(20.dp))
                            TimePicker(
                                state = timePickerState,
                                layoutType = TimePickerLayoutType.Vertical
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = { showTimePicker = false }) {
                                    Text("Отмена")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                TextButton(onClick = {
                                    showTimePicker = false
                                }) {
                                    Text("OK")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun FormRowIncome(
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
fun FormInputRowIncome(
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
fun AddNewIncomeScreenPreview() {
    FinGidTheme {
        AddEditExpenseScreen(navController = rememberNavController(), expenseId = null)
    }
}

@Preview(showBackground = true)
@Composable
fun EditIncomeScreenPreview() {
    FinGidTheme {
        AddEditExpenseScreen(navController = rememberNavController(), expenseId = "some_id_123")
    }
}