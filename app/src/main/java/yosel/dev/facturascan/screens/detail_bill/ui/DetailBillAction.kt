package yosel.dev.facturascan.screens.detail_bill.ui

sealed interface DetailBillAction {

    data object OnDismissErrorDialog: DetailBillAction

    data class OnChangeValueFormState(val value: String, val field: Int): DetailBillAction
}