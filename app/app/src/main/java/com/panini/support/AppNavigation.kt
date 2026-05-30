package com.panini.support

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.panini.support.ui.screens.createticket.CreateTicketScreen
import com.panini.support.ui.screens.login.LoginScreen
import com.panini.support.ui.screens.ticketdetail.TicketDetailScreen
import com.panini.support.ui.screens.ticketlist.TicketListScreen

private object Routes {
    const val LOGIN = "login"
    const val TICKET_LIST = "ticket_list"
    const val TICKET_DETAIL = "ticket_detail/{ticketId}"
    const val CREATE_TICKET = "create_ticket"

    fun ticketDetail(ticketId: String) = "ticket_detail/$ticketId"
}

/**
 * Navigation graph for the full app flow.
 * All route strings are centralized in [Routes] — no raw strings in composables.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

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
                }
            )
        }

        composable(
            route = Routes.TICKET_DETAIL,
            arguments = listOf(navArgument("ticketId") { type = NavType.StringType })
        ) { backStackEntry ->
            val ticketId = backStackEntry.arguments?.getString("ticketId") ?: return@composable
            TicketDetailScreen(
                ticketId = ticketId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.CREATE_TICKET) {
            CreateTicketScreen(
                onTicketCreated = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
