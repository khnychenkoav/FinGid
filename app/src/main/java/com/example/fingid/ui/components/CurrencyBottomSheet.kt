package com.example.fingid.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HighlightOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fingid.ui.theme.DividerColor
import com.example.fingid.ui.theme.FinGidTheme
import com.example.fingid.ui.theme.Red
import com.example.fingid.ui.theme.White

data class Currency(
    val symbol: String,
    val name: String,
    val code: String
)

val availableCurrencies = listOf(
    Currency("₽", "Российский рубль", "RUB"),
    Currency("$", "Американский доллар", "USD"),
    Currency("€", "Евро", "EUR")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onCurrencySelected: (Currency) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .width(32.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            LazyColumn {
                items(availableCurrencies) { currency ->
                    CurrencyRow(
                        currency = currency,
                        onClick = {
                            onCurrencySelected(currency)
                            onDismiss()
                        }
                    )
                    HorizontalDivider(color = DividerColor, thickness = 1.dp)
                }
            }

            CancelRowAsListItem(
                onDismiss = onDismiss
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun CancelRowAsListItem(onDismiss: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(Red)
            .clickable { onDismiss() }
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.HighlightOff,
            contentDescription = "Отмена",
            tint = White,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Отмена",
            style = MaterialTheme.typography.bodyLarge,
            color = White
        )
    }
}

@Composable
fun CurrencyRow(currency: Currency, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = currency.symbol,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "${currency.name} ${currency.symbol}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun CurrencyBottomSheetPreview() {
    FinGidTheme {
        val sheetState = rememberModalBottomSheetState()
        CurrencyBottomSheet(
            sheetState = sheetState,
            onDismiss = {},
            onCurrencySelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyRowPreview() {
    FinGidTheme {
        Column {
            CurrencyRow(currency = availableCurrencies[0], onClick = {})
            HorizontalDivider()
            CurrencyRow(currency = availableCurrencies[1], onClick = {})
        }
    }
}