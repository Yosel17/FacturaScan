package yosel.dev.facturascan.screens.register.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import yosel.dev.facturascan.core.components.ErrorDialog
import yosel.dev.facturascan.core.components.LoadingDialog
import yosel.dev.facturascan.core.components.SnackBarError
import yosel.dev.facturascan.ui.theme.FacturaScanTheme

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    state: RegisterState,
    snackbarHostState: SnackbarHostState,
    onAction: (RegisterAction) -> Unit
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                SnackBarError(data = data)
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .imePadding()
        ){
            BodyRegister(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                state = state,
                onAction = onAction
            )
        }

        if (state.isLoading) {
            LoadingDialog(
                title = "Iniciando sesión...",
                subtitle = "Por favor espera mientras validamos tus credenciales",
                colorTitle = MaterialTheme.colorScheme.primary
            )
        }

        if (state.isError) {
            ErrorDialog(
                message = state.errorMessage,
                onDismissRequest = { onAction(RegisterAction.OnDismissErrorDialog) }
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun RegisterScreenPreview() {
    FacturaScanTheme {
        RegisterScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            state = RegisterState(),
            snackbarHostState = SnackbarHostState(),
            onAction = {}
        )
    }
}