package yosel.dev.facturascan.core.models.response

import com.google.firebase.firestore.DocumentId
import java.util.Date

data class BillResponse(
    @DocumentId
    val id: String = "",
    val createdAt: Date? = null,

    // Datos del emisor y cliente
    val companyName: String = "",
    val vendorTaxId: String = "",
    val customerTaxId: String = "",

    // Datos de identificación de la factura
    val invoiceNumber: String = "",
    val serialNumber: String = "",
    val authorizationNumber: String = "",

    // Montos y fecha de emisión
    val issueDate: String = "",    // Fecha de emisión (ej. "2026-07-25")
    val totalAmount: Double = 0.0,

    // Campos útiles para auditoría y escaneo OCR
    val description: String = "",
    val imageUrl: String = ""
)
