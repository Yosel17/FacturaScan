package yosel.dev.facturascan.screens.my_bills.ui

sealed interface MyBillsEvent {
    data class ShowSnackBarError(val message: String): MyBillsEvent
}