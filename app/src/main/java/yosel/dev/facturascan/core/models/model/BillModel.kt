package yosel.dev.facturascan.core.models.model

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
)
