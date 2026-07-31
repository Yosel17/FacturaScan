package yosel.dev.facturascan.screens.detail_bill.ui

sealed interface DetailBillAction {

    data object OnDismissErrorDialog: DetailBillAction

    data class OnChangeValueFormState(val value: String, val field: Int): DetailBillAction

    data object OnClickDelete: DetailBillAction

    data object OnDismissDeleteDialog: DetailBillAction

    data object ConfirmDelete: DetailBillAction

    data class OnCopyFieldClick(val label: String, val value: String) : DetailBillAction
}