package yosel.dev.facturascan.screens.detail_bill.ui

data class DetailBillState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val errorMessage: String = "",
)
