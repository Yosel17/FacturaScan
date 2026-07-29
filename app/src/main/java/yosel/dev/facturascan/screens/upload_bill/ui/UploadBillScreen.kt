package yosel.dev.facturascan.screens.upload_bill.ui

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import yosel.dev.facturascan.core.components.LoadingDialog
import yosel.dev.facturascan.core.components.PermissionRationaleDialog
import yosel.dev.facturascan.core.components.PermissionSettingsDialog
import yosel.dev.facturascan.core.components.SnackBarError
import yosel.dev.facturascan.core.components.TopBarGlobal
import yosel.dev.facturascan.core.utils.openAppSettings
import yosel.dev.facturascan.ui.theme.FacturaScanTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadBillScreen(
    modifier: Modifier = Modifier,
    state: UploadBillState,
    snackbarHostState: SnackbarHostState,
    onAction: (UploadBillAction) -> Unit
) {
    val context = LocalContext.current

    // Estado de transición de entrada para la pantalla
    // Al inicializarse en false y cambiar targetState a true, Compose inicia la animación de inmediato
    val screenVisibleState = remember {
        MutableTransitionState(initialState = false).apply {
            targetState = true
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState){ data ->
                SnackBarError(data = data)
            }
        },
        topBar = {
            TopBarGlobal(
                title = "Cargar Factura",
                onBack = {
                    onAction(UploadBillAction.OnBackClick)
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->

        // Animación de entrada principal
        AnimatedVisibility(
            visibleState = screenVisibleState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Transición interna según la presencia de la imagen (se mantiene intacta)
                AnimatedContent(
                    targetState = state.imageUri,
                    label = "InvoiceImageTransition",
                    modifier = Modifier.weight(1f)
                ) { uri ->
                    if (uri == null) {
                        UploadInvoiceBox(
                            onUploadClick = { onAction(UploadBillAction.OnUploadBillClick) }
                        )
                    } else {
                        PreviewInvoiceCard(
                            imageUri = uri,
                            onChangeClick = { onAction(UploadBillAction.OnUploadBillClick) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onAction(UploadBillAction.OnProcessBillClick) },
                    enabled = state.isProcessEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Icon(
                        imageVector = Icons.Default.DocumentScanner,
                        contentDescription = "Procesar Factura",
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Procesar Factura",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        // Diálogos y BottomSheets fuera de AnimatedVisibility para evitar problemas de jerarquía visual
        if (state.isBottomSheetVisible) {
            SourceSelectionBottomSheet(
                onDismiss = { onAction(UploadBillAction.OnDismissBottomSheet) },
                onSelectCamera = {
                    val hasPermission = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED

                    if (hasPermission) {
                        onAction(UploadBillAction.OnSelectCameraClick)
                    } else {
                        onAction(UploadBillAction.OnObtainPermits)
                    }
                },
                onSelectGallery = { onAction(UploadBillAction.OnSelectGalleryClick) }
            )
        }

        if (state.showRationaleDialog) {
            PermissionRationaleDialog(
                onDismiss = { onAction(UploadBillAction.OnToggleRationaleDialog(show = false)) },
                onConfirm = {
                    onAction(UploadBillAction.OnToggleRationaleDialog(show = false))
                    onAction(UploadBillAction.OnObtainPermits)
                }
            )
        }

        if (state.showSettingsDialog) {
            PermissionSettingsDialog(
                onDismiss = { onAction(UploadBillAction.OnToggleSettingsDialog(show = false)) },
                onGoToSettings = {
                    onAction(UploadBillAction.OnToggleSettingsDialog(show = false))
                    context.openAppSettings()
                }
            )
        }

        if (state.isLoading){
            LoadingDialog(
                title = "Escaneando factura...",
                subtitle = "Por favor espera mientras procesamos el documento"
            )
        }
    }
}

@PreviewScreenSizes
@Composable
private fun PreviewGlobal(modifier: Modifier = Modifier) {
    FacturaScanTheme {
        UploadBillScreen(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            state = UploadBillState(
                imageUri = null
            ),
            snackbarHostState = SnackbarHostState(),
            onAction = {}
        )
    }
}