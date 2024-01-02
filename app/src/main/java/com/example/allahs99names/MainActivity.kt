package com.example.allahs99names

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.allahs99names.presentation.names_list_screen.NamesListScreen
import com.example.allahs99names.presentation.navigation.Routes.NAMES_LIST_SCREEN_ROUTE
import com.example.allahs99names.presentation.navigation.Routes.START_SCREEN_ROUTE
import com.example.allahs99names.presentation.navigation.Routes.TRAINING_SCREEN_ROUTE
import com.example.allahs99names.presentation.start_screen.StartScreen
import com.example.allahs99names.presentation.training_screen.TrainingScreen
import com.example.allahs99names.ui.theme.Allahs99NamesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Allahs99NamesTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = START_SCREEN_ROUTE,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    composable(START_SCREEN_ROUTE) { StartScreen() }
                    composable(TRAINING_SCREEN_ROUTE) { TrainingScreen() }
                    composable(NAMES_LIST_SCREEN_ROUTE) { NamesListScreen() }
                }
            }
        }
    }
}
