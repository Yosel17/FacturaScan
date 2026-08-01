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
import yosel.dev.facturascan.screens.register.domain.RegisterRepository
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: RegisterRepository
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
        }
    }

    private fun registerUser() {
        if (!_state.value.isSubmitEnabled) return

        viewModelScope.launch {

            val cs = _state.value

            _state.update { it.copy(isLoading = true, enableFields = false) }

            repository.registerUser(name = cs.name, accessCode = cs.accessCode)
                .onSuccess {
                    _state.update { it.copy(isLoading = false, enableFields = true) }
                    _eventChannel.send(RegisterEvent.OnNavigation(Screens.MyBills))
                }.onFailure { error ->
                    _state.update { it.copy(isLoading = false, enableFields = true) }
                    _eventChannel.send(RegisterEvent.ShowErrorSnackbar(error.localizedMessage ?: "Error al acceder"))
                }

        }
    }
}