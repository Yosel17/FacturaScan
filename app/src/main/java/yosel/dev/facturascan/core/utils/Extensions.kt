package yosel.dev.facturascan.core.utils

import yosel.dev.facturascan.core.models.model.BillModel
import yosel.dev.facturascan.core.models.response.BillResponse
import java.text.DecimalFormat

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

fun Double.formatAmount(currencySymbol: String = "Q"): String {
    val formatter = DecimalFormat("$currencySymbol #,##0.##")
    return formatter.format(this)
}

fun List<BillResponse>.toBillListResponseToModel(): List<BillModel>{
    return map { it.toModel() }
}