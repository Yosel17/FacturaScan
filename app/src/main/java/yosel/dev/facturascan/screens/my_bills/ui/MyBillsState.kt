package yosel.dev.facturascan.screens.my_bills.ui

import yosel.dev.facturascan.core.models.model.BillModel
import yosel.dev.facturascan.core.utils.formatAmount

data class MyBillsState(
    val isError: Boolean = false,
    val errorMessage: String = "",
    val isLoading: Boolean = true,
    val myBills: List<BillModel> = emptyList(),
    val totalAmount: Double = 0.0
){
    // Propiedad calculada/formateada
    val formattedTotalAmount: String
        get() = totalAmount.formatAmount()
}
