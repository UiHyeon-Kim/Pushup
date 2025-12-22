package com.hanhyo.presentation.navigation

sealed class NavigationRoute(val route: String) {
    object Home : NavigationRoute(Route.HOME)
    object Pushup : NavigationRoute(Route.PUSHUP)
    object Record : NavigationRoute(Route.RECORD)
}
