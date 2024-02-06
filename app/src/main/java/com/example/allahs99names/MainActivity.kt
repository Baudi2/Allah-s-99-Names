package com.example.allahs99names

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.allahs99names.presentation.names_list_screen.NamesListScreen
import com.example.allahs99names.presentation.navigation.Routes.NAMES_LIST_SCREEN_ROUTE
import com.example.allahs99names.presentation.navigation.Routes.START_SCREEN_ROUTE
import com.example.allahs99names.presentation.navigation.Routes.TRAINING_SCREEN_ROUTE
import com.example.allahs99names.presentation.navigation.Screens
import com.example.allahs99names.presentation.start_screen.StartScreen
import com.example.allahs99names.presentation.trainings.TrainingScreen
import com.example.allahs99names.ui.theme.Allahs99NamesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bottomNavVisibleScreens = listOf(Screens.ScreenStart, Screens.ScreenNamesList)
        val bottomNavVisibleRoutes =
            listOf(Screens.ScreenStart.route, Screens.ScreenNamesList.route)
        setContent {
            Allahs99NamesTheme {
                val navController = rememberNavController()
                val currentBackStackEntry = navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStackEntry.value?.destination
                val currentRoute = currentDestination?.route
                Scaffold(
                    bottomBar = if (currentRoute in bottomNavVisibleRoutes) {
                        {
                            BottomNavigation(
                                backgroundColor = MaterialTheme.colorScheme.background
                            ) {
                                bottomNavVisibleScreens.forEach { screen ->
                                    val isSelected =
                                        currentDestination?.hierarchy?.any { it.route == screen.route } == true
                                    BottomNavigationItem(
                                        icon = {
                                            Icon(
                                                painter = painterResource(
                                                    id = if (isSelected) {
                                                        screen.bottomNavSelectedIcon ?: 0
                                                    } else {
                                                        screen.bottomNavUnselectedIcon ?: 0
                                                    }
                                                ),
                                                contentDescription = null,
                                                tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = stringResource(
                                                    id = screen.bottomNavText ?: 0
                                                ),
                                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
                                            )
                                        },
                                        selected = isSelected,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    } else {
                        {}
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = START_SCREEN_ROUTE,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        composable(START_SCREEN_ROUTE) { StartScreen(navController) }
                        composable(TRAINING_SCREEN_ROUTE) { TrainingScreen() }
                        composable(NAMES_LIST_SCREEN_ROUTE) { NamesListScreen() }
                    }
                }
            }
        }
    }
}
