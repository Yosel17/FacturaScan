package yosel.dev.facturascan.core.models.model

import yosel.dev.facturascan.core.utils.formatAmount
import yosel.dev.facturascan.core.utils.formatDate

data class BillModel(
    val id: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val companyName: String = "",
    val vendorTaxId: String = "",
    val customerTaxId: String = "",
    val invoiceNumber: String = "",
    val serialNumber: String = "",
    val authorizationNumber: String = "",
    val issueDate: String = "",
    val totalAmount: Double = 0.0,
    val description: String = "",
    val imageUrl: String = ""
){
    // Propiedades calculadas para la UI
    val formattedDate: String
        get() = createdAt.formatDate()

    val formattedAmount: String
        get() = totalAmount.formatAmount()
}
