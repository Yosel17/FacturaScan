package yosel.dev.facturascan.screens.detail_bill.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import yosel.dev.facturascan.core.models.model.BillModel
import yosel.dev.facturascan.core.room.tables.bill.BillDao
import yosel.dev.facturascan.core.utils.toModel
import yosel.dev.facturascan.screens.detail_bill.domain.DetailBillRepository
import javax.inject.Inject

class DetailBillRepositoryImpl @Inject constructor(
    private val billDao: BillDao
): DetailBillRepository {

    override suspend fun getBillById(id: String): Result<BillModel> {
        return withContext(Dispatchers.IO) {
            try {
                val billEntity = billDao.getBillById(id)
                    ?: throw Exception("Factura no encontrada en la base de datos local")
                val billModel = billEntity.toModel()
                Result.success(billModel)
            } catch (e: Exception) {
                Result.failure(exception = e)
            }
        }
    }
}