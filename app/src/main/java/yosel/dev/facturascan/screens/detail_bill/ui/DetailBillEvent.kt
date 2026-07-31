package yosel.dev.facturascan.screens.detail_bill.ui

sealed interface DetailBillEvent {

    data class ShowErrorSnackbar(val message: String) : DetailBillEvent
}