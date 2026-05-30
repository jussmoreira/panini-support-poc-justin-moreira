package com.panini.support

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.panini.support.ui.ViewModelFactory
import com.panini.support.ui.screens.createticket.CreateTicketScreen
import com.panini.support.ui.screens.login.LoginScreen
import com.panini.support.ui.screens.settings.SettingsScreen
import com.panini.support.ui.screens.ticketdetail.TicketDetailScreen
import com.panini.support.ui.screens.ticketlist.TicketListScreen

private object Routes {
    const val LOGIN = "login"
    const val TICKET_LIST = "ticket_list"
    const val TICKET_DETAIL = "ticket_detail/{ticketId}"
    const val CREATE_TICKET = "create_ticket"
    const val SETTINGS = "settings"

    fun ticketDetail(ticketId: String) = "ticket_detail/$ticketId"
}

/**
 * Navigation graph for the full app flow.
 * All route strings are centralized in [Routes] — no raw strings in composables.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current.applicationContext as PaniniApplication
    val factory = ViewModelFactory(context.container.ticketRepository)

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.TICKET_LIST) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.TICKET_LIST) {
            TicketListScreen(
                onTicketClick = { ticketId ->
                    navController.navigate(Routes.ticketDetail(ticketId))
                },
                onCreateTicket = {
                    navController.navigate(Routes.CREATE_TICKET)
                },
                onSettings = {
                    navController.navigate(Routes.SETTINGS)
                },
                viewModel = viewModel(factory = factory)
            )
        }

        composable(
            route = Routes.TICKET_DETAIL,
            arguments = listOf(navArgument("ticketId") { type = NavType.StringType })
        ) { backStackEntry ->
            val ticketId = backStackEntry.arguments?.getString("ticketId") ?: return@composable
            TicketDetailScreen(
                ticketId = ticketId,
                onBack = { navController.popBackStack() },
                viewModel = viewModel(factory = factory)
            )
        }

        composable(Routes.CREATE_TICKET) {
            CreateTicketScreen(
                onTicketCreated = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
                viewModel = viewModel(factory = factory)
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
