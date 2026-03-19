package com.proyek.kalender.ui.navigation

sealed class Screen(val route: String) {
    data object Schedule : Screen("schedule")
    data object Calendar : Screen("calendar")
    data object AddEvent : Screen("add_event")

    data object EventDetail : Screen("event_detail/{eventId}") {

        fun createRoute(eventId: String) = "event_detail/$eventId"
    }
}