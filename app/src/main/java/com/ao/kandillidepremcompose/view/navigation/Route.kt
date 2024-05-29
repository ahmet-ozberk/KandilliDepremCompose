package com.ao.kandillidepremcompose.view.navigation

sealed class Route(val route: String) {
    object Home : Route("home")
    object Detail : Route("detail_map/{kandilli}")
}