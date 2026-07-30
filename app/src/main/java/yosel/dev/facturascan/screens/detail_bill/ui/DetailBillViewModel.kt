package yosel.dev.facturascan.screens.detail_bill.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import yosel.dev.facturascan.core.models.model.BillModel
import yosel.dev.facturascan.core.utils.Constants
import yosel.dev.facturascan.screens.detail_bill.domain.DetailBillRepository

@HiltViewModel(assistedFactory = DetailBillViewModel.Factory::class)
class DetailBillViewModel @AssistedInject constructor(
    private val repository: DetailBillRepository,
    @Assisted private val idBill: String
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(idBill: String): DetailBillViewModel
    }

    private val _state = MutableStateFlow(DetailBillState())
    val state: StateFlow<DetailBillState> = _state

    init {
        getBill(idBill = idBill)
    }

    fun onAction(action: DetailBillAction){
        when(action){
            DetailBillAction.OnDismissErrorDialog -> {
                _state.update { it.copy(isError = false, errorMessage = "") }
            }

            is DetailBillAction.OnChangeValueFormState -> {
                onValueFormStateChange(action.value, action.field)
            }
        }
    }

    private fun getBill(idBill: String){
        viewModelScope.launch {
            repository.getBillById(id = idBill)
                .onSuccess { billModel ->
                    successGetBill(billModel)
                }.onFailure { error ->
                    _state.update {
                        it.copy(
                            errorMessage = "No pudimos cargar la información de la factura. Comprueba tu conexión a internet e inténtalo de nuevo.",
                            isError = true,
                            isLoading = false
                        )
                    }
                }
        }
    }

    private fun successGetBill(bill: BillModel){
        _state.update {
            it.copy(
                currentBill = bill,
                formState = DetailBillFormState(
                    serialNumber = bill.serialNumber,
                    billNumber = bill.invoiceNumber,
                    issueDate = bill.issueDate,
                    vendorTaxId = bill.vendorTaxId,
                    customerTaxId = bill.customerTaxId,
                    totalAmount = bill.totalAmount.toString(),
                    description = bill.description
                ),
                isLoading = false
            )
        }
    }

    private fun onValueFormStateChange(value: String, field: Int){
        when(field){
            Constants.SERIAL_NUMBER_FIELD -> _state.update { it.copy(formState = it.formState.copy(serialNumber = value)) }
            Constants.BILL_NUMBER_FIELD -> _state.update { it.copy(formState = it.formState.copy(billNumber = value)) }
            Constants.ISSUE_DATE_FIELD -> _state.update { it.copy(formState = it.formState.copy(issueDate = value)) }
            Constants.VENDOR_TAX_ID_FIELD -> _state.update { it.copy(formState = it.formState.copy(vendorTaxId = value)) }
            Constants.CUSTOMER_TAX_ID -> _state.update { it.copy(formState = it.formState.copy(customerTaxId = value)) }
            Constants.TOTAL_AMOUNT_FIELD -> _state.update { it.copy(formState = it.formState.copy(totalAmount = value)) }
            Constants.DESCRIPTION_FIELD -> _state.update { it.copy(formState = it.formState.copy(description = value)) }
        }
    }
}