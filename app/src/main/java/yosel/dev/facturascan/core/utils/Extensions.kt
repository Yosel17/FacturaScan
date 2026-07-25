package yosel.dev.facturascan.core.utils

import yosel.dev.facturascan.core.models.model.BillModel
import yosel.dev.facturascan.core.models.response.BillResponse

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