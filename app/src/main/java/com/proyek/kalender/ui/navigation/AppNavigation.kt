package com.proyek.kalender.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.proyek.kalender.ui.components.EditorialBottomNav
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

    // Sembunyikan bottom bar dan FAB di layar detail & form
    val showBottomNav = currentRoute == Screen.Schedule.route || currentRoute == Screen.Calendar.route

    Scaffold(
        modifier = modifier,
        containerColor = Color(0xFFFCF8FF),
        bottomBar = {
            if (showBottomNav) {
                EditorialBottomNav(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.Schedule.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            if (showBottomNav) {
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.AddEvent.route) },
                    containerColor = Color(0xFF4956B4),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Event")
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = Screen.Calendar.route // Set kalender sebagai home
            ) {
                composable(route = Screen.Schedule.route) {
                    ScheduleScreen(
                        onEventClick = { eventId -> navController.navigate(Screen.EventDetail.createRoute(eventId)) }
                    )
                }
                composable(route = Screen.Calendar.route) {
                    CalendarScreen(
                        onEventClick = { eventId -> navController.navigate(Screen.EventDetail.createRoute(eventId)) }
                    )
                }
                composable(route = Screen.AddEvent.route) {
                    AddEventScreen(onNavigateBack = { navController.popBackStack() })
                }
                composable(
                    route = Screen.EventDetail.route,
                    arguments = listOf(navArgument("eventId") { type = NavType.StringType })
                ) {
                    EventDetailScreen(onNavigateBack = { navController.popBackStack() })
                }
            }
        }
    }
}