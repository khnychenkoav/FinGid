package com.example.fingid.ui.screens

import android.icu.lang.UCharacter
import android.icu.lang.UProperty
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fingid.domain.model.Category
import com.example.fingid.ui.theme.Black
import com.example.fingid.ui.theme.DividerColor
import com.example.fingid.ui.theme.FinGidTheme
import com.example.fingid.ui.theme.LightGreen
import com.example.fingid.ui.theme.SearchBackgroundColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticlesScreen(
    // В будущем сюда будет приходить ViewModel
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    // TODO: Этот список нужно будет получать из ViewModel
    val categories = remember {
        sampleCategories()
    }

    val filteredCategories = categories.filter {
        it.textLeading.contains(searchQuery.text, ignoreCase = true)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Мои статьи", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = { Text("Найти статью") },
                trailingIcon = {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Иконка поиска"
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    unfocusedContainerColor = SearchBackgroundColor,
                    disabledContainerColor = SearchBackgroundColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
            )

            HorizontalDivider(
                color = DividerColor,
                thickness = 1.dp,
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredCategories, key = { it.id }) { category ->
                    ArticleRow(category = category)
                    HorizontalDivider(
                        color = DividerColor,
                        thickness = 1.dp,
                    )
                }
            }
        }
    }
}

@Composable
fun ArticleRow(category: Category) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(LightGreen),
            contentAlignment = Alignment.Center
        ) {
            val displayText = if (isEmoji(category.iconLeading)) {
                category.iconLeading
            } else {
                category.textLeading
                    .split(' ')
                    .filter { it.isNotBlank() }
                    .take(2)
                    .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                    .joinToString("")
            }

            Text(
                text = displayText,
                style = TextStyle(
                    fontSize = if (isEmoji(displayText)) 16.sp else 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = Black,
                    textAlign = TextAlign.Center
                ),
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = category.textLeading,
            style = MaterialTheme.typography.bodyLarge,
            color = Black
        )
    }
}

@RequiresApi(Build.VERSION_CODES.P)
fun isEmoji(text: String): Boolean {
    if (text.isEmpty()) return false
    val codePoints = text.codePoints().toArray()
    return codePoints.all { cp ->
        UCharacter.hasBinaryProperty(cp, UProperty.EMOJI)
                || cp == 0x200D
                || cp == 0xFE0F
    }
}

private fun sampleCategories(): List<Category> {
    return listOf(
        Category(1, "🏠", "Аренда квартиры", false),
        Category(2, "👗", "Одежда", false),
        Category(3, "🐶", "На собачку", false),
        Category(4, "🐶", "На собачку", false),
        Category(5, "РК", "Ремонт квартиры", false),
        Category(6, "🍭", "Продукты", false),
        Category(7, "🏋️", "Спортзал", false),
        Category(8, "💊", "Медицина", false),
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ArticlesScreenPreview() {
    FinGidTheme(darkTheme = false) {
        ArticlesScreen()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ArticleRowPreview() {
    FinGidTheme(darkTheme = false) {
        Column {
            ArticleRow(Category(1, "🏠", "Аренда квартиры", false))
            ArticleRow(Category(2, "РМ", "Ремонт Машины", false))
            ArticleRow(Category(3, "🍕", "Еда", false))
        }
    }
}