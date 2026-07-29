package yosel.dev.facturascan.screens.detail_bill.domain

import yosel.dev.facturascan.core.models.model.BillModel

interface DetailBillRepository {

    suspend fun getBillById(id: String): Result<BillModel>
}