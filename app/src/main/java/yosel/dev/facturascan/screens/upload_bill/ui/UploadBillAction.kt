package yosel.dev.facturascan.screens.upload_bill.ui

import android.net.Uri

sealed interface UploadBillAction {

    data object OnBackClick : UploadBillAction
    data object OnUploadBillClick : UploadBillAction
    data object OnDismissBottomSheet : UploadBillAction
    data object OnSelectCameraClick : UploadBillAction
    data object OnSelectGalleryClick : UploadBillAction
    data class OnImageSelected(val uri: Uri?) : UploadBillAction
    data object OnProcessBillClick : UploadBillAction
    data object OnObtainPermits : UploadBillAction
    data class OnToggleRationaleDialog(val show: Boolean) : UploadBillAction
    data class OnToggleSettingsDialog(val show: Boolean) : UploadBillAction
}