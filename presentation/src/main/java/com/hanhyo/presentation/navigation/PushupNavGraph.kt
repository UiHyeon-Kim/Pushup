package com.hanhyo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hanhyo.presentation.ui.home.HomeScreen
import com.hanhyo.presentation.ui.pushup.PushupScreen
import com.hanhyo.presentation.ui.record.PushupRecordScreen
import com.hanhyo.presentation.ui.setting.SettingScreen

@Composable
fun PushupNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavigationRoute.Home.route,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(NavigationRoute.Home.route) { HomeScreen(navController) }
        composable(NavigationRoute.Pushup.route) { PushupScreen() }
        composable(NavigationRoute.Record.route) { PushupRecordScreen() }
        composable(NavigationRoute.Setting.route) { SettingScreen() }
    }
}
