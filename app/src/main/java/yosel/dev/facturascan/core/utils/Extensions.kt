package yosel.dev.facturascan.core.utils

import yosel.dev.facturascan.core.models.model.BillModel
import yosel.dev.facturascan.core.models.response.BillResponse
import java.text.DecimalFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun BillResponse.toModel(): BillModel{
    return BillModel(
        id = id,
        createdAt = createdAt,
        companyName = companyName,
        vendorTaxId = vendorTaxId,
        customerTaxId = customerTaxId,
        invoiceNumber = invoiceNumber,
        serialNumber = serialNumber,
        authorizationNumber = authorizationNumber,
        issueDate = issueDate,
        totalAmount = totalAmount,
        description = description,
        imageUrl = imageUrl
    )
}
fun List<BillResponse>.toBillListResponseToModel(): List<BillModel>{
    return map { it.toModel() }
}

// Formateador de moneda (reutilizable)
private val currencyFormatExact = DecimalFormat("Q#,##0")
private val currencyFormatDecimal = DecimalFormat("Q#,##0.00")

fun Double.formatAmount(): String {
    return if (this % 1.0 == 0.0) {
        currencyFormatExact.format(this)
    } else {
        currencyFormatDecimal.format(this)
    }
}

// Formateador de fecha moderno e inmutable
private val spanishLocale = Locale.forLanguageTag("es-ES")
private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yy", spanishLocale)

fun Long.formatDate(): String {
    if (this <= 0L) return "sin fecha"

    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .format(dateFormatter)
        .lowercase()
}