package yosel.dev.facturascan.core.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

            entry<Screens.MyBills> {
                Scaffold() { paddingValues ->
                    Column(
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        Text(text = "My Bills")
                        Button(onClick = { backStack.add(Screens.UploadBill) }) {
                            Text(text = "Upload Bill")
                        }

                    }
                }
            }

            entry<Screens.UploadBill> {
                Scaffold() { paddingValues ->
                    Column(
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        Text(text = "Upload Bill")
                        Button(onClick = { backStack.add(Screens.DetailBill) }) {
                            Text(text = "Detail Bill")
                        }
                    }
                }
            }

            entry<Screens.DetailBill> {
                Scaffold() { paddingValues ->
                    Column(
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        Text(text = "Detail Bill")
                        Button(onClick = { backStack.removeLastOrNull() }) {
                            Text(text = "back")
                        }
                    }
                }
            }
        }
    )
}