package yosel.dev.facturascan.screens.detail_bill.ui

import yosel.dev.facturascan.core.models.model.BillModel

data class DetailBillState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val currentBill: BillModel = BillModel(),
    val formState: DetailBillFormState = DetailBillFormState(),
    val showDialogDelete: Boolean = false,
    val warningMessage: String = "",
    val isLoadingDeleteBill: Boolean = false,
    val isLoadingSaveBill: Boolean = false,
    val isLoadingUpdateBill: Boolean = false
){
    // Evalúa si se ha modificado al menos un campo respecto a la factura original
    val isFormModified: Boolean
        get() = formState.billNumber != currentBill.invoiceNumber ||
                formState.serialNumber != currentBill.serialNumber ||
                formState.issueDate != currentBill.issueDate ||
                formState.vendorTaxId != currentBill.vendorTaxId ||
                formState.customerTaxId != currentBill.customerTaxId ||
                formState.totalAmount != currentBill.totalAmount.toString() ||
                formState.description != currentBill.description

    // Para GUARDAR (Borrador): Debe tener todos los campos requeridos llenos
    val isSaveEnabled: Boolean
        get() = formState.isValid && !isLoadingSaveBill

    // Para EDITAR: Debe tener todos los campos requeridos llenos Y detectar al menos un cambio respecto al origen
    val isEditEnabled: Boolean
        get() = formState.isValid && isFormModified && !isLoadingUpdateBill
}
