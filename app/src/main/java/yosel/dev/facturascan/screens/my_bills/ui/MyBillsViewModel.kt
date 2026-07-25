package yosel.dev.facturascan.screens.my_bills.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import yosel.dev.facturascan.screens.my_bills.domain.MyBillsRepository
import javax.inject.Inject

@HiltViewModel
class MyBillsViewModel @Inject constructor(
    private val repository: MyBillsRepository
): ViewModel() {

    private val _state = MutableStateFlow(MyBillsState())
    val state: StateFlow<MyBillsState> = _state

    init {
        getMyBills()
    }

    private fun getMyBills() {
        viewModelScope.launch {
            repository.getAllBills()
                .onSuccess { bills ->
                    _state.update {
                        it.copy(
                            myBills = bills,
                            isLoading = false
                        )
                    }
                }.onFailure { error ->
                    _state.update {
                        it.copy(
                            errorMessage = error.localizedMessage ?: "Error desconocido",
                            isError = true,
                            isLoading = false
                        )
                    }
                }
        }
    }
}