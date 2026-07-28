package yosel.dev.facturascan.screens.upload_bill.ui

import android.net.Uri

data class UploadBillState(
    val imageUri: Uri? = null,
    val isBottomSheetVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
){
    val isProcessEnabled: Boolean
        get() = imageUri != null && !isLoading
}
