package yosel.dev.facturascan.screens.upload_bill.ui

import yosel.dev.facturascan.core.navigation.Screens

sealed interface UploadBillEvent {

    data class ShowErrorSnackbar(val message: String) : UploadBillEvent
    data object NavigateBack : UploadBillEvent
    data object LaunchCamera : UploadBillEvent
    data object LaunchGallery : UploadBillEvent
    data object LaunchPermission : UploadBillEvent

    data class OnNavigation(val screen: Screens) : UploadBillEvent
}