package yosel.dev.facturascan.screens.my_bills.ui

import yosel.dev.facturascan.core.models.model.BillModel

data class MyBillsState(
    val isError: Boolean = false,
    val errorMessage: String = "",
    val isLoading: Boolean = true,
    val myBills: List<BillModel> = emptyList()
)
