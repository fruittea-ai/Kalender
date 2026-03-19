package com.proyek.kalender.ui.navigation

sealed class Screen(val route: String) {
    data object Schedule : Screen("schedule")
    data object Calendar : Screen("calendar")
    data object AddEvent : Screen("add_event")

    // Rute sekarang membutuhkan parameter {eventId}
    data object EventDetail : Screen("event_detail/{eventId}") {
        // Fungsi bantuan (helper) untuk membuat URL rute dengan ID yang spesifik
        fun createRoute(eventId: String) = "event_detail/$eventId"
    }
}