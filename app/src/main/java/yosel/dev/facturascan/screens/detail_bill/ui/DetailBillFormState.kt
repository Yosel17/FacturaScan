package yosel.dev.facturascan.screens.detail_bill.ui

data class DetailBillFormState(
    val serialNumber: String = "",
    val billNumber: String = "",
    val issueDate: String = "",
    val vendorTaxId: String = "",
    val customerTaxId: String = "",
    val totalAmount: String = "",
    val description: String = ""
){
    val formattedCopyText: String
        get() = buildString {
            if (billNumber.isNotBlank()) appendLine("Número de Factura: $billNumber")
            if (serialNumber.isNotBlank()) appendLine("Número de Serie: $serialNumber")
            if (issueDate.isNotBlank()) appendLine("Fecha de Emisión: $issueDate")
            if (vendorTaxId.isNotBlank()) appendLine("NIT del Proveedor: $vendorTaxId")
            if (customerTaxId.isNotBlank()) appendLine("NIT Organización: $customerTaxId")
            if (totalAmount.isNotBlank()) appendLine("Total: $totalAmount")
            if (description.isNotBlank()) appendLine("Descripción: $description")
        }.trimEnd()
}
