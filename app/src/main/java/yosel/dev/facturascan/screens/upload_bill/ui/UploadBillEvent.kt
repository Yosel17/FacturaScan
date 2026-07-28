package yosel.dev.facturascan.screens.upload_bill.ui

sealed interface UploadBillEvent {

    data class ShowSnackbar(val message: String) : UploadBillEvent
    data object NavigateBack : UploadBillEvent
    data object LaunchCamera : UploadBillEvent
    data object LaunchGallery : UploadBillEvent
}