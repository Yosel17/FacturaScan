package yosel.dev.facturascan.core.navigation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.launch
import yosel.dev.facturascan.core.utils.ObserveAsEvents
import yosel.dev.facturascan.screens.my_bills.ui.MyBillsEvent
import yosel.dev.facturascan.screens.my_bills.ui.MyBillsScreen
import yosel.dev.facturascan.screens.my_bills.ui.MyBillsViewModel
import yosel.dev.facturascan.screens.upload_bill.ui.UploadBillAction
import yosel.dev.facturascan.screens.upload_bill.ui.UploadBillEvent
import yosel.dev.facturascan.screens.upload_bill.ui.UploadBillScreen
import yosel.dev.facturascan.screens.upload_bill.ui.UploadBillViewModel
import java.io.File

fun EntryProviderScope<NavKey>.myBillsEntry(
    onNavigate: (Screens) -> Unit
) {
    entry<Screens.MyBills> {
        val viewmodel = hiltViewModel<MyBillsViewModel>()
        val state by viewmodel.state.collectAsStateWithLifecycle()
        val snackBarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        ObserveAsEvents(viewmodel.events) { event ->
            when(event){
                is MyBillsEvent.ShowSnackBarError -> {
                    scope.launch {
                        snackBarHostState.showSnackbar(event.message)
                    }
                }
            }
        }

        MyBillsScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            state = state,
            snackBarHostState = snackBarHostState,
            onNavigation = { screen ->
                onNavigate(screen)
            }
        )
    }
}

fun EntryProviderScope<NavKey>.uploadBillEntry(
    onNavigate: (Screens) -> Unit,
    onBack: () -> Unit
) {
    entry<Screens.UploadBill> {
        val viewModel: UploadBillViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val snackbarHostState = remember { SnackbarHostState() }
        val context = LocalContext.current
        var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

        val galleryLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            viewModel.onAction(UploadBillAction.OnImageSelected(uri))
        }

        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { success: Boolean ->
            if (success && tempCameraUri != null) {
                viewModel.onAction(UploadBillAction.OnImageSelected(tempCameraUri))
            }
        }

        ObserveAsEvents(viewModel.events) { event ->
            when (event) {
                is UploadBillEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                UploadBillEvent.NavigateBack -> {
                    onBack()
                }
                UploadBillEvent.LaunchGallery -> {
                    galleryLauncher.launch("image/*")
                }
                UploadBillEvent.LaunchCamera -> {
                    val file = File.createTempFile("invoice_", ".jpg", context.cacheDir)
                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        file
                    )
                    tempCameraUri = uri
                    cameraLauncher.launch(uri)
                }
            }
        }

        UploadBillScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            state = state,
            snackbarHostState = snackbarHostState,
            onAction = viewModel::onAction
        )
    }
}

fun EntryProviderScope<NavKey>.detailBillEntry(
    onBack: () -> Unit
) {
    entry<Screens.DetailBill> {
        Scaffold() { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                Text(text = "Detail Bill")
                Button(onClick = { onBack() }) {
                    Text(text = "back")
                }
            }
        }
    }
}