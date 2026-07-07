package com.example.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object Routes {
    const val USER_PANEL = "user_panel"
    const val ADMIN_DASHBOARD = "admin_dashboard"
    const val PROFILE_MANAGEMENT = "profile_management"
    const val ADMIN_SETTINGS = "admin_settings"
}

@Composable
fun AppNavigation(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.USER_PANEL,
        modifier = modifier
    ) {
        composable(Routes.USER_PANEL) {
            UserPanelScreen(
                viewModel = viewModel,
                onNavigateToAdmin = { navController.navigate(Routes.ADMIN_DASHBOARD) }
            )
        }
        composable(Routes.ADMIN_DASHBOARD) {
            AdminDashboardScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToProfiles = { navController.navigate(Routes.PROFILE_MANAGEMENT) },
                onNavigateToSettings = { navController.navigate(Routes.ADMIN_SETTINGS) }
            )
        }
        composable(Routes.PROFILE_MANAGEMENT) {
            ProfileManagementScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Routes.ADMIN_SETTINGS) {
            AdminSettingsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
