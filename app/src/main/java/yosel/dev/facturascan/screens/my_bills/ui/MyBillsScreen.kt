package yosel.dev.facturascan.screens.my_bills.ui

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import yosel.dev.facturascan.core.components.SnackBarError
import yosel.dev.facturascan.core.components.TopBarGlobal

@Composable
fun MyBillsScreen(
    modifier: Modifier = Modifier,
    state: MyBillsState,
    snackBarHostState: SnackbarHostState
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState){ data ->
                SnackBarError(data = data)
            }
        },
        topBar = {
            TopBarGlobal(
                title = "FacturaScan"
            )
        }
    ) { }
}