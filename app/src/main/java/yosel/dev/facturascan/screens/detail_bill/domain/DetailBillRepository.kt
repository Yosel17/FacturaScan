package yosel.dev.facturascan.screens.detail_bill.domain

import yosel.dev.facturascan.core.models.model.BillModel

interface DetailBillRepository {

    suspend fun getBillById(id: String): Result<BillModel>

    suspend fun saveBillLocalAdnFirestore(bill: BillModel): Result<Unit>

    suspend fun deleteBillRoom(idBill: String): Result<Unit>

    suspend fun deleteBillFirestore(idBill: String): Result<Unit>
}