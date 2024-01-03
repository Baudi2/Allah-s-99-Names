package com.example.allahs99names.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.allahs99names.R
import com.example.allahs99names.presentation.navigation.Routes.NAMES_LIST_SCREEN_ROUTE
import com.example.allahs99names.presentation.navigation.Routes.START_SCREEN_ROUTE
import com.example.allahs99names.presentation.navigation.Routes.TRAINING_SCREEN_ROUTE

sealed class Screens(
    val route: String,
    @StringRes val bottomNavText: Int? = null,
    @DrawableRes val bottomNavIcon: Int? = null
) {
    data object ScreenStart : Screens(
        route = START_SCREEN_ROUTE,
        bottomNavText = R.string.start_screen_bottom_nav_tab_title,
        bottomNavIcon = R.drawable.start_training_bottom_nav_icon
    )

    data object ScreenNamesList : Screens(
        route = NAMES_LIST_SCREEN_ROUTE,
        bottomNavText = R.string.names_list_screen_bottom_nav_tab_title,
        bottomNavIcon = R.drawable.start_training_bottom_nav_icon
    )

    data object ScreenTraining : Screens(TRAINING_SCREEN_ROUTE)
}