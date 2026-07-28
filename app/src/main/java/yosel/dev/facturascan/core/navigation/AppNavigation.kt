package yosel.dev.facturascan.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay

@Composable
fun AppNavigation(startDestination: Screens) {

    val backStack = rememberNavBackStack(startDestination)


    NavDisplay(
        backStack = backStack,
        onBack = {
            backStack.removeLastOrNull()
        },
        entryProvider = entryProvider {

            myBillsEntry(
                onNavigate = { screen ->
                    backStack.add(screen)
                }
            )

            uploadBillEntry(
                onNavigate = { screen ->
                    backStack.add(screen)
                },
                onBack = {
                    backStack.removeLastOrNull()
                }
            )

            detailBillEntry(
                onBack = {
                    backStack.removeLastOrNull()
                }
            )
        }
    )
}