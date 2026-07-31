package yosel.dev.facturascan.screens.detail_bill.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import yosel.dev.facturascan.core.models.model.BillModel
import yosel.dev.facturascan.core.utils.Constants
import yosel.dev.facturascan.screens.detail_bill.domain.DetailBillRepository
import kotlin.time.Duration.Companion.milliseconds

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

    private val _eventChannel = Channel<DetailBillEvent>()
    val events = _eventChannel.receiveAsFlow()

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
            DetailBillAction.ConfirmDelete -> {
                deleteBill()
            }
            DetailBillAction.OnClickDelete -> {
                showDialogError()
            }
            DetailBillAction.OnDismissDeleteDialog -> {
                _state.update { it.copy(showDialogDelete = false, warningMessage = "") }
            }

            is DetailBillAction.OnCopyFieldClick -> {
                copyFieldToClipboard(action.label, action.value)
            }

            DetailBillAction.OnCopyAllClick -> {
                copyAllFields()
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

    private fun showDialogError(){
        val warningText = if (_state.value.currentBill.status == Constants.DRAFT_STATUS)
            "Esta acción eliminará permanentemente este borrador. No podrás recuperarlo."
        else
            "¿Estás seguro de eliminar esta factura? Esta acción es permanente y no se puede deshacer."

        _state.update { it.copy(warningMessage = warningText, showDialogDelete = true) }
    }

    private fun deleteBill(){
        _state.update {
            it.copy(showDialogDelete = false, warningMessage = "", isLoadingDeleteBill = true)
        }

        viewModelScope.launch {
            repository.deleteBillRoom(idBill = _state.value.currentBill.id)
                .onSuccess {
                    if (_state.value.currentBill.status == Constants.DRAFT_STATUS){
                        _state.update { it.copy(isLoadingDeleteBill = false) }
                        _eventChannel.send(
                            DetailBillEvent.ShowSuccessSnackbar("Factura eliminada con éxito")
                        )
                        delay(1000.milliseconds)
                        _eventChannel.send(DetailBillEvent.NavigateBack)
                    }else {
                        deleteBillFirestore()
                    }
                }.onFailure { error ->
                    _state.update {
                        it.copy(isLoadingDeleteBill = false)
                    }
                    _eventChannel.send(
                        element = DetailBillEvent.ShowErrorSnackbar(
                            "No pudimos eliminar la factura del dispositivo. Inténtalo de nuevo."
                        )
                    )
                }
        }
    }

    private suspend fun deleteBillFirestore(){
        repository.deleteBillFirestore(_state.value.currentBill.id)
            .onSuccess {
                _state.update { it.copy(isLoadingDeleteBill = false) }
                _eventChannel.send(
                    DetailBillEvent.ShowSuccessSnackbar("Factura eliminada con éxito")
                )
                delay(1000.milliseconds)
                _eventChannel.send(DetailBillEvent.NavigateBack)
            }.onFailure {
                _state.update {
                    it.copy(isLoadingDeleteBill = false)
                }
                _eventChannel.send(
                    element = DetailBillEvent.ShowErrorSnackbar(
                        "No pudimos eliminar la factura de la nube. Inténtalo de nuevo."
                    )
                )
            }
    }

    private fun copyFieldToClipboard(label: String, value: String) {
        if (value.isBlank()) return
        viewModelScope.launch {
            _eventChannel.send(
                DetailBillEvent.ShowSuccessSnackbar("$label copiado al portapapeles")
            )
        }
    }

    private fun copyAllFields() {
        val textToCopy = _state.value.formState.formattedCopyText

        if (textToCopy.isEmpty()) return

        viewModelScope.launch {
            _eventChannel.send(
                DetailBillEvent.ShowSuccessSnackbar("Todos los datos fueron copiados")
            )
        }
    }
}