package yosel.dev.facturascan.screens.detail_bill.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import yosel.dev.facturascan.screens.detail_bill.domain.DetailBillRepository
import javax.inject.Inject

@HiltViewModel
class DetailBillViewModel @Inject constructor(
    private val repository: DetailBillRepository
): ViewModel() {

    private val _state = MutableStateFlow(DetailBillState())
    val state: StateFlow<DetailBillState> = _state


}