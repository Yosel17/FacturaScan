package yosel.dev.facturascan.core.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey

fun EntryProviderScope<NavKey>.myBillsEntry(
    onNavigate: (Screens) -> Unit
) {
    entry<Screens.MyBills> {
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                Text(text = "My Bills")
                Button(onClick = { onNavigate(Screens.UploadBill) }) {
                    Text(text = "Upload Bill")
                }
            }
        }
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