package yosel.dev.facturascan.core.models.ai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BillAiResponse(
    @SerialName("company_name")
    val companyName: String = "",
    @SerialName("vendor_tax_id")
    val vendorTaxId: String = "",
    @SerialName("customer_tax_id")
    val customerTaxId: String = "",
    @SerialName("invoice_number")
    val invoiceNumber: String = "",
    @SerialName("serial_number")
    val serialNumber: String = "",
    @SerialName("authorization_number")
    val authorizationNumber: String = "",
    @SerialName("issue_date")
    val issueDate: String = "",
    @SerialName("total_amount")
    val totalAmount: Double = 0.0,
    @SerialName("description")
    val description: String = ""
)
