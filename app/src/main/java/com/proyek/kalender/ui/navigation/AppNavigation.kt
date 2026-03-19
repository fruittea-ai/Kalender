package com.proyek.kalender.ui.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.proyek.kalender.ui.components.AppNavigationRail
import com.proyek.kalender.ui.screens.addevent.AddEventScreen
import com.proyek.kalender.ui.screens.calendar.CalendarScreen
import com.proyek.kalender.ui.screens.detail.EventDetailScreen
import com.proyek.kalender.ui.screens.schedule.ScheduleScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Schedule.route

    Scaffold(
        modifier = modifier,
        containerColor = Color(0xFFFCF8FF)
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AppNavigationRail(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Schedule.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )

            // Pastikan ketiga layar (Schedule, Calendar, AddEvent) ada di dalam NavHost
            NavHost(
                navController = navController,
                startDestination = Screen.Schedule.route,
                modifier = Modifier.weight(1f)
            ) {
                composable(route = Screen.Schedule.route) {
                    ScheduleScreen(onEventClick = { eventId ->
                        navController.navigate(Screen.EventDetail.createRoute(eventId))
                        }
                    )
                }
                composable(route = Screen.Calendar.route) {
                    CalendarScreen()
                }
                composable(route = Screen.AddEvent.route) {
                    AddEventScreen(
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                }
                composable(route = Screen.EventDetail.route, arguments = listOf(
                    navArgument("eventId") { type = NavType.StringType }
                )) {
                    EventDetailScreen(
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}