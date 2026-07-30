package yosel.dev.facturascan.core.models.request

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class BillRequest(
    val id: String,
    @ServerTimestamp
    val createdAt: Date?,

    // Datos del emisor y cliente
    val companyName: String,
    val vendorTaxId: String,
    val customerTaxId: String,

    // Datos de identificación
    val invoiceNumber: String,
    val serialNumber: String,
    val authorizationNumber: String,

    // Montos y fecha de emisión
    val issueDate: String,
    val totalAmount: Double,

    // Campos adicionales
    val description: String,
    val imageUrl: String,
    val status: Int
)
