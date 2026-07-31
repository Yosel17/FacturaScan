package yosel.dev.facturascan.screens.detail_bill.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import yosel.dev.facturascan.core.components.CustomSnackbarHost
import yosel.dev.facturascan.core.components.DeleteConfirmationDialog
import yosel.dev.facturascan.core.components.ErrorDialog
import yosel.dev.facturascan.core.components.LoadingDialog
import yosel.dev.facturascan.core.components.SnackBarError
import yosel.dev.facturascan.core.components.TopBarGlobal
import yosel.dev.facturascan.core.models.model.BillModel
import yosel.dev.facturascan.ui.theme.FacturaScanTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DetailBillScreen(
    modifier: Modifier = Modifier,
    state: DetailBillState,
    snackBarHostState: SnackbarHostState,
    onAction: (DetailBillAction) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            CustomSnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            TopBarGlobal(
                title = "Detalle Factura",
                onBack = onBack,
                actions = {
                    if (state.currentBill.id.isNotEmpty()){
                        IconButton(
                            onClick = {
                                onAction(DetailBillAction.OnClickDelete)
                            }
                        ) {

                            Icon(
                                imageVector = Icons.Rounded.Delete,
                                contentDescription = "Eliminar",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .imePadding()
        ){
            AnimatedContent(
                targetState = state,
                contentKey = { targetState ->
                    when{
                        targetState.isLoading -> "LOADING"
                        targetState.currentBill.id.isEmpty() -> "EMPTY"
                        else -> "CONTENT"
                    }
                },
                label = "DetailBillScreenAnimation"
            ) { targetState ->
                when{
                    targetState.isLoading ->{
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            LoadingIndicator(
                                modifier = Modifier.size(75.dp)
                            )
                        }
                    }
                    targetState.currentBill.id.isEmpty() -> {
                        EmptyBillDetailsState()
                    }
                    else ->{
                        BodyDetailBill(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 24.dp),
                            state = state,
                            onAction = onAction
                        )
                    }
                }
            }
        }

        if (state.isError){
            ErrorDialog(
                message = state.errorMessage,
                onDismissRequest = {
                    onAction(DetailBillAction.OnDismissErrorDialog)
                }
            )
        }

        if (state.showDialogDelete){
            DeleteConfirmationDialog(
                warningMessage = state.warningMessage,
                onDismissRequest = {
                    onAction(DetailBillAction.OnDismissDeleteDialog)
                },
                onConfirmDelete = {
                    onAction(DetailBillAction.ConfirmDelete)
                }
            )
        }

        if (state.isLoadingDeleteBill){
            LoadingDialog(
                title = "Eliminando factura...",
                subtitle = "Por favor espera un momento mientras eliminamos la factura",
                color = MaterialTheme.colorScheme.error,
                colorTitle = MaterialTheme.colorScheme.error
            )
        }

        if (state.isLoadingSaveBill){
            LoadingDialog(
                title = "Guardando factura...",
                subtitle = "Por favor espera un momento mientras guardamos la factura",
                color = MaterialTheme.colorScheme.primary,
                colorTitle = MaterialTheme.colorScheme.primary
            )
        }

        if (state.isLoadingUpdateBill){
            LoadingDialog(
                title = "Actualizando factura...",
                subtitle = "Por favor espera un momento mientras guardamos los cambios.",
                color = MaterialTheme.colorScheme.primary,
                colorTitle = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    FacturaScanTheme {
        DetailBillScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            state = DetailBillState(
                isLoading = false,
                currentBill = BillModel(id = "adfkjasdfkas")
            ),
            snackBarHostState = SnackbarHostState(),
            onAction = {},
            onBack = {}
        )
    }
}