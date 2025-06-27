package com.example.fingid.ui.commonitems

import com.example.fingid.R
import com.example.fingid.navigation.Screen

sealed class BottomNavigationItem(
    val screen: Screen,
    val titleResId: Int,
    val iconResId: Int,
) {
    object Expenses : BottomNavigationItem(Screen.Expenses, R.string.bottom_nav_expenses, R.drawable.ic_downtrend)
    object Income : BottomNavigationItem(Screen.Income, R.string.bottom_nav_income, R.drawable.ic_uptrend)
    object Account : BottomNavigationItem(Screen.Account, R.string.bottom_nav_account, R.drawable.ic_calculator)
    object Articles : BottomNavigationItem(Screen.Articles, R.string.bottom_nav_articles, R.drawable.ic_barchartside)
    object Settings : BottomNavigationItem(Screen.Settings, R.string.bottom_nav_settings, R.drawable.ic_settings)
}