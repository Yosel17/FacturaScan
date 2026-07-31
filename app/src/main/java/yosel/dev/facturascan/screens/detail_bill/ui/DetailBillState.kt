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
)
