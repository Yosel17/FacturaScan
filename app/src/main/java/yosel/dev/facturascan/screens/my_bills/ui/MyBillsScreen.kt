package yosel.dev.facturascan.screens.my_bills.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import yosel.dev.facturascan.core.components.SnackBarError
import yosel.dev.facturascan.core.components.TopBarGlobal
import yosel.dev.facturascan.core.models.model.BillModel
import yosel.dev.facturascan.ui.theme.FacturaScanTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
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
        },
        floatingActionButton = {
            if (!state.isLoading){
                ExtendedFloatingActionButton(
                    onClick = {},
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Photo,
                            contentDescription = "Photo"
                        )
                    },
                    text = { Text(text = "Escanear Factura") },
                    expanded = true
                )
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            when{
                state.isLoading ->{
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        LoadingIndicator(
                            modifier = Modifier.size(75.dp)
                        )
                    }
                }
                else ->{
                    BodyMyBills(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp),
                        state = state,
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Screen() {
    FacturaScanTheme{
        MyBillsScreen(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            state = MyBillsState(
                isLoading = true,
                totalAmount = 1234.56,
                myBills = listOf(
                    BillModel(
                        id = "1",
                        companyName = "Amazon Services",
                        invoiceNumber = "123456",
                        serialNumber = "A",
                        createdAt = 1781438400000L,
                        totalAmount = 10.00
                    ),
                    BillModel(
                        id = "2",
                        companyName = "Amazon Services",
                        invoiceNumber = "123456",
                        serialNumber = "A",
                        createdAt = 1781438400000L,
                        totalAmount = 456.78
                    )
                )
            ),
            snackBarHostState = SnackbarHostState()
        )
    }
}