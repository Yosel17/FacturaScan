package yosel.dev.facturascan.core.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay

@Composable
fun AppNavigation(startDestination: Screens) {

    val backStack = rememberNavBackStack(startDestination)


    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(), // Para el estado UI
            rememberViewModelStoreNavEntryDecorator()       // Para aislar los ViewModels
        ),
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
                    if (screen is Screens.DetailBill) {
                        backStack.removeLastOrNull()
                    }
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

            registerEntry(
                onNavigate = { screen ->
                    backStack.add(screen)
                }
            )
        }
    )
}