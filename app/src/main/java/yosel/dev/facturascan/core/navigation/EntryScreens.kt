package yosel.dev.facturascan.core.navigation

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import yosel.dev.facturascan.screens.my_bills.ui.MyBillsScreen
import yosel.dev.facturascan.screens.my_bills.ui.MyBillsViewModel

fun EntryProviderScope<NavKey>.myBillsEntry(
    onNavigate: (Screens) -> Unit
) {
    entry<Screens.MyBills> {
        val viewmodel = hiltViewModel<MyBillsViewModel>()
        val state by viewmodel.state.collectAsStateWithLifecycle()
        val snackBarHostState = remember { SnackbarHostState() }

        MyBillsScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            state = state,
            snackBarHostState = snackBarHostState
        )
    }
}

fun EntryProviderScope<NavKey>.uploadBillEntry(
    onNavigate: (Screens) -> Unit
) {
    entry<Screens.UploadBill> {
        Scaffold() { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                Text(text = "Upload Bill")
                Button(onClick = { onNavigate(Screens.DetailBill) }) {
                    Text(text = "Detail Bill")
                }
            }
        }
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