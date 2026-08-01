package yosel.dev.facturascan.screens.register.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import yosel.dev.facturascan.core.navigation.Screens
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    // private val repository: RegisterRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state

    private val _eventChannel = Channel<RegisterEvent>()
    val events = _eventChannel.receiveAsFlow()

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.OnNameChanged -> {
                _state.update { it.copy(name = action.name) }
            }
            is RegisterAction.OnAccessCodeChanged -> {
                _state.update { it.copy(accessCode = action.code) }
            }
            RegisterAction.OnTogglePasswordVisibility -> {
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }
            RegisterAction.OnSubmitClick -> {
                registerUser()
            }
            RegisterAction.OnDismissErrorDialog -> {
                _state.update { it.copy(isError = false, errorMessage = "") }
            }
        }
    }

    private fun registerUser() {
        if (!_state.value.isSubmitEnabled) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            // =========================================================================
            // CAPA DE DATOS Y DOMINIO PENDIENTE
            // Ejemplo de llamada futura al repositorio:
            //
            // repository.registerUser(name = _state.value.name, code = _state.value.accessCode)
            //     .onSuccess {
            //         _state.update { it.copy(isLoading = false) }
            //         _eventChannel.send(RegisterEvent.OnNavigation(Screens.MyBills))
            //     }
            //     .onFailure { error ->
            //         _state.update { it.copy(isLoading = false) }
            //         _eventChannel.send(RegisterEvent.ShowErrorSnackbar(error.localizedMessage ?: "Error al acceder"))
            //     }
            // =========================================================================

            // Simulación temporal para navegación UI
            _state.update { it.copy(isLoading = false) }
            _eventChannel.send(RegisterEvent.OnNavigation(Screens.MyBills))
        }
    }
}