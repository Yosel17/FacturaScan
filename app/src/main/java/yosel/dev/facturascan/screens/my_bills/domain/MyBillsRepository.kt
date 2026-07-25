package yosel.dev.facturascan.screens.my_bills.domain

import yosel.dev.facturascan.core.models.model.BillModel

interface MyBillsRepository {

    suspend fun getAllBills(): Result<List<BillModel>>
}