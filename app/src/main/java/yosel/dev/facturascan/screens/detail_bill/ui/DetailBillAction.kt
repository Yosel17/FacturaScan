package yosel.dev.facturascan.screens.detail_bill.ui

sealed interface DetailBillAction {

    data object OnDismissErrorDialog: DetailBillAction
}