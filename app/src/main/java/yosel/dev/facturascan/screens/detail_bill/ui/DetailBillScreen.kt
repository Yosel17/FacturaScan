package yosel.dev.facturascan.screens.detail_bill.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import yosel.dev.facturascan.core.components.SnackBarError
import yosel.dev.facturascan.core.components.TopBarGlobal

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DetailBillScreen(
    modifier: Modifier = Modifier,
    state: DetailBillState,
    snackBarHostState: SnackbarHostState,
    onBack: () -> Unit
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
                title = "Detalle Factura",
                onBack = onBack,
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.errorContainer
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                            state = state
                        )
                    }
                }
            }
        }
    }
}