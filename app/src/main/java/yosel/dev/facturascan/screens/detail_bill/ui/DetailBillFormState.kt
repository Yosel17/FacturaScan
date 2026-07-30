package yosel.dev.facturascan.screens.detail_bill.ui

data class DetailBillFormState(
    val serialNumber: String = "",
    val billNumber: String = "",
    val issueDate: String = "",
    val vendorTaxId: String = "",
    val customerTaxId: String = "",
    val totalAmount: String = "",
    val description: String = ""
)
