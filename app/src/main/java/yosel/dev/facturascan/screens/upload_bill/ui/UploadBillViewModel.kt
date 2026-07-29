package yosel.dev.facturascan.screens.upload_bill.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import yosel.dev.facturascan.screens.upload_bill.domain.UploadBillRepository
import javax.inject.Inject

@HiltViewModel
class UploadBillViewModel @Inject constructor(
    private val repository: UploadBillRepository
): ViewModel() {

    private val _state = MutableStateFlow(UploadBillState())
    val state: StateFlow<UploadBillState> = _state

    private val _eventChannel = Channel<UploadBillEvent>()
    val events = _eventChannel.receiveAsFlow()

    fun onAction(action: UploadBillAction) {
        when (action) {
            UploadBillAction.OnBackClick -> {
                viewModelScope.launch {
                    _eventChannel.send(UploadBillEvent.NavigateBack)
                }
            }
            UploadBillAction.OnUploadBillClick -> {
                _state.update { it.copy(isBottomSheetVisible = true) }
            }
            UploadBillAction.OnDismissBottomSheet -> {
                _state.update { it.copy(isBottomSheetVisible = false) }
            }
            UploadBillAction.OnSelectCameraClick -> {
                _state.update { it.copy(isBottomSheetVisible = false) }
                viewModelScope.launch {
                    _eventChannel.send(UploadBillEvent.LaunchCamera)
                }
            }
            UploadBillAction.OnSelectGalleryClick -> {
                _state.update { it.copy(isBottomSheetVisible = false) }
                viewModelScope.launch {
                    _eventChannel.send(UploadBillEvent.LaunchGallery)
                }
            }
            UploadBillAction.OnObtainPermits -> {
                _state.update { it.copy(isBottomSheetVisible = false) }
                viewModelScope.launch {
                    _eventChannel.send(UploadBillEvent.LaunchPermission)
                }
            }
            is UploadBillAction.OnImageSelected -> {
                _state.update { it.copy(imageUri = action.uri) }
            }
            UploadBillAction.OnProcessBillClick -> {
                processInvoice()
            }
            is UploadBillAction.OnToggleRationaleDialog -> {
                _state.update { it.copy(showRationaleDialog = action.show) }
            }
            is UploadBillAction.OnToggleSettingsDialog -> {
                _state.update { it.copy(showSettingsDialog = action.show) }
            }
        }
    }

    private fun processInvoice(){
        val currentUri = _state.value.imageUri ?: return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            repository.processInvoice(currentUri)
                .onSuccess { billModel ->
                    _state.update { it.copy(isLoading = false) }
                    _eventChannel.send(
                        UploadBillEvent.ShowSnackbar("Factura procesada: ${billModel.companyName}")
                    )
                    println("YoselBug: $billModel")
                    // TODO: Aquí puedes guardar en Firestore o navegar al detalle según tu flujo
                }
                .onFailure { error ->
                    Log.e("UploadBillViewModel", "Error al procesar la factura", error)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.localizedMessage ?: "Error al procesar la factura"
                        )
                    }
                    _eventChannel.send(
                        UploadBillEvent.ShowSnackbar(
                            error.localizedMessage ?: "Ocurrió un error al procesar la factura"
                        )
                    )
                }
        }
    }

}