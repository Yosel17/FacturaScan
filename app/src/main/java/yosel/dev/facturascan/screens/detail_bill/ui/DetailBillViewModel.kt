package yosel.dev.facturascan.screens.detail_bill.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import yosel.dev.facturascan.core.navigation.Screens
import yosel.dev.facturascan.screens.detail_bill.domain.DetailBillRepository
import javax.inject.Inject

@HiltViewModel
class DetailBillViewModel @Inject constructor(
    private val repository: DetailBillRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val navArgs = savedStateHandle.toRoute<Screens.DetailBill>()

    private val _state = MutableStateFlow(DetailBillState())
    val state: StateFlow<DetailBillState> = _state

    init {
        getBill(idBill = navArgs.idBill)
    }

    private fun getBill(idBill: String){
        viewModelScope.launch {
            repository.getBillById(id = idBill)
                .onSuccess { billModel ->
                    _state.update {
                        it.copy(
                            currentBill = billModel,
                            isLoading = false
                        )
                    }
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
}