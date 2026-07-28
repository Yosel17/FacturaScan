package yosel.dev.facturascan.screens.upload_bill.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import yosel.dev.facturascan.core.components.TopBarGlobal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadBillScreen(
    modifier: Modifier = Modifier,
    state: UploadBillState,
    snackbarHostState: SnackbarHostState,
    onAction: (UploadBillAction) -> Unit
) {

    val context = LocalContext.current

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

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
                Text(
                    text = "Procesar Factura",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        if (state.isBottomSheetVisible) {
            SourceSelectionBottomSheet(
                onDismiss = { onAction(UploadBillAction.OnDismissBottomSheet) },
                onSelectCamera = {
                    val hasPermission = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED

                    if (hasPermission){
                        onAction(UploadBillAction.OnSelectCameraClick)
                    }else{
                        onAction(UploadBillAction.OnObtainPermits)
                    }

                },
                onSelectGallery = { onAction(UploadBillAction.OnSelectGalleryClick) }
            )
        }
    }
}