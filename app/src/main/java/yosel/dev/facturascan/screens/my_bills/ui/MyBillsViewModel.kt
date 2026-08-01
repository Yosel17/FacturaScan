package yosel.dev.facturascan.screens.my_bills.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import yosel.dev.facturascan.screens.my_bills.domain.MyBillsRepository
import javax.inject.Inject

@HiltViewModel
class MyBillsViewModel @Inject constructor(
    private val repository: MyBillsRepository
): ViewModel() {

    private val _state = MutableStateFlow(MyBillsState())
    val state: StateFlow<MyBillsState> = repository.getAllBills()
        .catch { error ->
            // Manejo de errores de lectura en base de datos local
            _events.send(MyBillsEvent.ShowSnackBarError("Error al leer la base de datos local"))
        }
        .combine(_state) { bills, localState ->
            val total = bills.sumOf { it.totalAmount }
            localState.copy(
                myBills = bills,
                totalAmount = total
            )
        }
        .stateIn(
            scope = viewModelScope,
            // WhileSubscribed(5000) detiene la recolección 5s después de que la UI deje de ser visible
            // Evita seguir procesando si el usuario gira la pantalla o pasa la App a segundo plano.
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MyBillsState(isLoading = true)
        )

    private val _events = Channel<MyBillsEvent>()
    val events = _events.receiveAsFlow()

    init {
        // Al iniciar, leemos de Room inmediatamente (mediante stateIn) y desencadenamos la sync remota.
        fetchRemoteBills()
    }

    private fun fetchRemoteBills() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            repository.syncBills()
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false) }
                    _events.send(
                        MyBillsEvent.ShowSnackBarError(
                            message = error.localizedMessage ?: "Error al sincronizar las facturas"
                        )
                    )
                }
        }
    }
}