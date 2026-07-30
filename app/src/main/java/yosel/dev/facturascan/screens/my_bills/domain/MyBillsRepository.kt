package yosel.dev.facturascan.screens.my_bills.domain

import kotlinx.coroutines.flow.Flow
import yosel.dev.facturascan.core.models.model.BillModel

interface MyBillsRepository {

    fun getAllBills(): Flow<List<BillModel>>

    suspend fun syncBills(): Result<Unit>
}